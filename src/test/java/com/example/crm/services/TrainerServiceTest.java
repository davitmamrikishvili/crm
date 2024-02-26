package com.example.crm.services;

import com.example.crm.model.Trainer;
import com.example.crm.model.User;
import com.example.crm.repositories.TrainerRepository;
import com.example.crm.repositories.UserRepository;
import com.example.crm.service.TrainerService;
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
public class TrainerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setActive(true);

        trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainerId(1L);
    }

    @Test
    public void selectTrainer_existingUsername_returnsTrainer() {
        when(trainerRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainer));
        Trainer result = trainerService.selectTrainer(user.getUsername());
        assertNotNull(result);
        assertEquals(trainer, result);
    }

    @Test
    public void selectTrainer_nonExistingUsername_throwsIllegalArgumentException() {
        when(trainerRepository.findByUserUsername("nonExisting")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> trainerService.selectTrainer("nonExisting"));
    }

    @Test
    public void authenticate_withCorrectCredentials_returnsTrue() {
        when(trainerRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainer));
        assertTrue(trainerService.authenticate(user.getUsername(), "password"));
    }

    @Test
    public void authenticate_withIncorrectCredentials_returnsFalse() {
        when(trainerRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainer));
        assertFalse(trainerService.authenticate(user.getUsername(), "wrongPassword"));
    }

    @Test
    public void changePassword_withCorrectCredentials_changesPassword() throws AuthenticationException {
        when(trainerRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainer));
        trainerService.changePassword(trainer, "newPassword");
        assertEquals("newPassword", trainer.getUser().getPassword());
    }

    @Test
    public void deactivate_deactivatesTrainer() throws AuthenticationException {
        when(trainerRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainer));
        trainerService.deactivate(trainer);
        assertFalse(trainer.getUser().isActive());
    }

    @Test
    public void createTrainer_createsTrainerWithUniqueUsername() {
        when(userRepository.numberOfUsersWithSameUsername(user.getUsername())).thenReturn(10);
        trainerService.createTrainer(trainer);
        assert(trainer.getUser().getUsername().equals("testUser-10"));
        verify(trainerRepository).save(trainer);
    }

    @Test
    public void updateTrainer_updatesTrainer() throws AuthenticationException {
        when(trainerRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(trainer));
        trainerService.updateTrainer(trainer);
        verify(trainerRepository).save(trainer);
    }
}
