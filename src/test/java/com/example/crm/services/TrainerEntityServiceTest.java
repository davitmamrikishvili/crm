package com.example.crm.services;

import com.example.crm.model.entities.TrainerEntity;
import com.example.crm.model.entities.UserEntity;
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
public class TrainerEntityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    private TrainerEntity trainerEntity;
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("password");
        userEntity.setActive(true);

        trainerEntity = new TrainerEntity();
        trainerEntity.setUserEntity(userEntity);
        trainerEntity.setTrainerId(1L);
    }

    @Test
    public void selectTrainer_existingUsername_returnsTrainer() {
        when(trainerRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(trainerEntity));
        TrainerEntity result = trainerService.selectTrainer(userEntity.getUsername());
        assertNotNull(result);
        assertEquals(trainerEntity, result);
    }

    @Test
    public void selectTrainer_nonExistingUsername_throwsIllegalArgumentException() {
        when(trainerRepository.findByUserEntityUsername("nonExisting")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> trainerService.selectTrainer("nonExisting"));
    }

    @Test
    public void authenticate_withCorrectCredentials_returnsTrue() {
        when(trainerRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(trainerEntity));
        assertTrue(trainerService.authenticate(userEntity.getUsername(), "password"));
    }

    @Test
    public void authenticate_withIncorrectCredentials_returnsFalse() {
        when(trainerRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(trainerEntity));
        assertFalse(trainerService.authenticate(userEntity.getUsername(), "wrongPassword"));
    }

    @Test
    public void changePassword_withCorrectCredentials_changesPassword() throws AuthenticationException {
        when(trainerRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(trainerEntity));
        trainerService.changePassword(trainerEntity, "newPassword");
        assertEquals("newPassword", trainerEntity.getUserEntity().getPassword());
    }

    @Test
    public void deactivate_deactivatesTrainer() throws AuthenticationException {
        when(trainerRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(trainerEntity));
        trainerService.deactivate(trainerEntity);
        assertFalse(trainerEntity.getUserEntity().isActive());
    }

    @Test
    public void createTrainer_createsTrainerWithUniqueUsername() {
        when(userRepository.numberOfUsersWithSameUsername(userEntity.getUsername())).thenReturn(10);
        trainerService.createTrainer(trainerEntity);
        assert(trainerEntity.getUserEntity().getUsername().equals("testUser-10"));
        verify(trainerRepository).save(trainerEntity);
    }

    @Test
    public void updateTrainer_updatesTrainer() throws AuthenticationException {
        when(trainerRepository.findByUserEntityUsername(userEntity.getUsername())).thenReturn(Optional.of(trainerEntity));
        trainerService.updateTrainer(trainerEntity);
        verify(trainerRepository).save(trainerEntity);
    }
}
