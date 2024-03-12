package com.example.crm.services;

import com.example.crm.model.entities.TraineeEntity;
import com.example.crm.model.entities.UserEntity;
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
public class TraineeEntityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeService traineeService;

    private TraineeEntity traineeEntity;
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("password");
        userEntity.setActive(true);

        traineeEntity = new TraineeEntity();
        traineeEntity.setUserEntity(userEntity);
        traineeEntity.setTraineeId(1L);
    }

    @Test
    public void selectTrainee_existingUsername_returnsTrainee() {
        when(traineeRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(traineeEntity));
        TraineeEntity result = traineeService.selectTrainee(userEntity.getUsername());
        assertNotNull(result);
        assertEquals(traineeEntity, result);
    }

    @Test
    public void selectTrainee_nonExistingUsername_throwsIllegalArgumentException() {
        when(traineeRepository.findByUserEntityUsername("nonExisting")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> traineeService.selectTrainee("nonExisting"));
    }

    @Test
    public void authenticate_withCorrectCredentials_returnsTrue() {
        when(traineeRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(traineeEntity));
        assertTrue(traineeService.authenticate(userEntity.getUsername(), "password"));
    }

    @Test
    public void authenticate_withIncorrectCredentials_returnsFalse() {
        when(traineeRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(traineeEntity));
        assertFalse(traineeService.authenticate(userEntity.getUsername(), "wrongPassword"));
    }

    @Test
    public void changePassword_withCorrectCredentials_changesPassword() throws AuthenticationException {
        when(traineeRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(traineeEntity));
        traineeService.changePassword(traineeEntity, "newPassword");
        assertEquals("newPassword", traineeEntity.getUserEntity().getPassword());
    }

    @Test
    public void deactivate_deactivatesTrainee() throws AuthenticationException {
        when(traineeRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(traineeEntity));
        traineeService.deactivate(traineeEntity);
        assertFalse(traineeEntity.getUserEntity().isActive());
    }

    @Test
    public void createTrainee_createsTraineeWithUniqueUsername() {
        when(userRepository.numberOfUsersWithSameUsername(userEntity.getUsername())).thenReturn(10);
        traineeService.createTrainee(traineeEntity);
        assert(traineeEntity.getUserEntity().getUsername().equals("testUser-10"));
        verify(traineeRepository).save(traineeEntity);
    }

    @Test
    public void updateTrainee_updatesTrainee() throws AuthenticationException {
        when(traineeRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(traineeEntity));
        traineeService.updateTrainee(traineeEntity);
        verify(traineeRepository).save(traineeEntity);
    }

    @Test
    public void deleteTrainee_deletesTrainee() {
        traineeService.deleteTrainee(userEntity.getUsername());
        verify(userRepository).deleteByUsername(userEntity.getUsername());
    }
}
