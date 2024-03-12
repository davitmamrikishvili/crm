package com.example.crm.service;

import com.example.crm.model.entities.TraineeEntity;
import com.example.crm.model.entities.TrainerEntity;
import com.example.crm.repositories.TrainerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TrainerService {

    private final UserService userService;
    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerService(UserService userService, TrainerRepository trainerRepository) {
        this.userService = userService;
        this.trainerRepository = trainerRepository;
    }

    public TrainerEntity selectTrainer(String username) {
        Optional<TrainerEntity> trainer = trainerRepository.findByUserEntityUsername(username);
        if (trainer.isEmpty()) {
            throw new IllegalArgumentException("Trainer with username " + username + " does not exist!");
        } else {
            return trainer.get();
        }
    }

    public boolean authenticate(String username, String password) {
        TrainerEntity trainerEntity = selectTrainer(username);
        return trainerEntity.getUserEntity().getPassword().equals(password);
    }

    public void changePassword(TrainerEntity trainerEntity, String password) throws AuthenticationException {
        if (trainerEntity == null) throw new IllegalArgumentException("Argument trainer must not be null!");
        if (!authenticate(trainerEntity.getUserEntity().getUsername(), trainerEntity.getUserEntity().getPassword())) throw new AuthenticationException("Wrong credentials");
        TrainerEntity trainerEntity1 = selectTrainer(trainerEntity.getUserEntity().getUsername());
        trainerEntity1.getUserEntity().setPassword(password);
        trainerRepository.save(trainerEntity1);
        log.info("Trainer with id " + trainerEntity.getTrainerId() + " and username " + trainerEntity.getUserEntity().getUsername() + " has changed password");
    }

    public void toggle(TrainerEntity trainerEntity) throws AuthenticationException {
        if (trainerEntity == null) throw new IllegalArgumentException("Argument trainer must not be null!");
        if (!authenticate(trainerEntity.getUserEntity().getUsername(), trainerEntity.getUserEntity().getPassword())) throw new AuthenticationException("Wrong credentials");
        TrainerEntity trainerEntity1 = selectTrainer(trainerEntity.getUserEntity().getUsername());
        boolean isActive = trainerEntity1.getUserEntity().isActive();
        trainerEntity1.getUserEntity().setActive(!isActive);
        trainerRepository.save(trainerEntity1);
        log.info("Trainer with id " + trainerEntity1.getTrainerId() + " and username " + trainerEntity1.getUserEntity().getUsername() + " has been deactivated");
    }

    public TrainerEntity createTrainer(TrainerEntity trainerEntity) {
        if (trainerEntity == null) throw new IllegalArgumentException("Argument trainer must not be null!");
        userService.createUser(trainerEntity.getUserEntity());
        String username = trainerEntity.getUserEntity().getUsername();
        log.info("Trainee with id " + trainerEntity.getTrainerId() + " and username " + username + " has been created");
        return trainerRepository.save(trainerEntity);
    }

    public void updateTrainer(TrainerEntity trainerEntity) throws AuthenticationException {
        if (trainerEntity == null) throw new IllegalArgumentException("Argument trainer must not be null!");
        if (!authenticate(trainerEntity.getUserEntity().getUsername(), trainerEntity.getUserEntity().getPassword())) throw new AuthenticationException("Wrong credentials");
        trainerRepository.save(trainerEntity);
        log.info("Trainer with id " + trainerEntity.getTrainerId() + " has been updated");
    }
}
