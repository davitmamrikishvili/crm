package com.example.crm.services;

import com.example.crm.model.Trainee;
import com.example.crm.model.User;
import com.example.crm.repositories.TraineeRepository;
import com.example.crm.repositories.UserRepository;
import com.example.crm.service.TraineeService;
import org.apache.tomcat.websocket.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setActive(true);

        trainee = new Trainee();
        trainee.setUser(user);
        trainee.setTraineeId(1L);
    }

    @Test
    public void selectTrainee_existingUsername_returnsTrainee() {
        when(traineeRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainee));
        Trainee result = traineeService.selectTrainee(user.getUsername());
        assertNotNull(result);
        assertEquals(trainee, result);
    }

    @Test
    public void selectTrainee_nonExistingUsername_throwsIllegalArgumentException() {
        when(traineeRepository.findByUserUsername("nonExisting")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> traineeService.selectTrainee("nonExisting"));
    }

    @Test
    public void authenticate_withCorrectCredentials_returnsTrue() {
        when(traineeRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainee));
        assertTrue(traineeService.authenticate(user.getUsername(), "password"));
    }

    @Test
    public void authenticate_withIncorrectCredentials_returnsFalse() {
        when(traineeRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainee));
        assertFalse(traineeService.authenticate(user.getUsername(), "wrongPassword"));
    }

    @Test
    public void changePassword_withCorrectCredentials_changesPassword() throws AuthenticationException {
        when(traineeRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainee));
        traineeService.changePassword(trainee, "newPassword");
        assertEquals("newPassword", trainee.getUser().getPassword());
    }

    @Test
    public void deactivate_deactivatesTrainee() throws AuthenticationException {
        when(traineeRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainee));
        traineeService.deactivate(trainee);
        assertFalse(trainee.getUser().isActive());
    }

    @Test
    public void createTrainee_createsTraineeWithUniqueUsername() {
        when(userRepository.numberOfUsersWithSameUsername(user.getUsername())).thenReturn(10);
        traineeService.createTrainee(trainee);
        assert(trainee.getUser().getUsername().equals("testUser-10"));
        verify(traineeRepository).save(trainee);
    }

    @Test
    public void updateTrainee_updatesTrainee() throws AuthenticationException {
        when(traineeRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainee));
        traineeService.updateTrainee(trainee);
        verify(traineeRepository).save(trainee);
    }

    @Test
    public void deleteTrainee_deletesTrainee() {
        traineeService.deleteTrainee(user.getUsername());
        verify(userRepository).deleteByUsername(user.getUsername());
    }
}
