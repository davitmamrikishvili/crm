package com.example.crm.service;

import com.example.crm.model.entities.TraineeEntity;
import com.example.crm.model.entities.TrainerEntity;
import com.example.crm.repositories.TraineeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class TraineeService {

    private final UserService userService;
    private final TraineeRepository traineeRepository;

    @Autowired
    public TraineeService(UserService userService, TraineeRepository traineeRepository) {
        this.userService = userService;
        this.traineeRepository = traineeRepository;
    }

    public TraineeEntity selectTrainee(String username) {
        Optional<TraineeEntity> trainee = traineeRepository.findByUserEntityUsername(username);
        if (trainee.isEmpty()) {
            throw new IllegalArgumentException("Trainee with username " + username + " does not exist!");
        } else {
            return trainee.get();
        }
    }

    public boolean authenticate(String username, String password) {
        TraineeEntity traineeEntity = selectTrainee(username);
        return traineeEntity.getUserEntity().getPassword().equals(password);
    }

    public boolean changePassword(TraineeEntity traineeEntity, String password) throws AuthenticationException {
        if (traineeEntity == null) throw new IllegalArgumentException("Argument trainee must not be null!");
        if (!authenticate(traineeEntity.getUserEntity().getUsername(), traineeEntity.getUserEntity().getPassword())) throw new AuthenticationException("Wrong credentials");
        TraineeEntity traineeEntity1 = selectTrainee(traineeEntity.getUserEntity().getUsername());
        traineeEntity1.getUserEntity().setPassword(password);
        traineeRepository.save(traineeEntity1);
        log.info("Trainee with id " + traineeEntity1.getTraineeId() + " and username " + traineeEntity1.getUserEntity().getUsername() + " has changed password");
        return true;
    }
    
    public void toggle(TraineeEntity traineeEntity) throws AuthenticationException {
        if (traineeEntity == null) throw new IllegalArgumentException("Argument trainee must not be null!");
        if (!authenticate(traineeEntity.getUserEntity().getUsername(), traineeEntity.getUserEntity().getPassword())) throw new AuthenticationException("Wrong credentials");
        TraineeEntity traineeEntity1 = selectTrainee(traineeEntity.getUserEntity().getUsername());
        boolean isActive = traineeEntity1.getUserEntity().isActive();
        traineeEntity1.getUserEntity().setActive(!isActive);
        traineeRepository.save(traineeEntity1);
        log.info("Trainee with id " + traineeEntity1.getTraineeId() + " and username " + traineeEntity1.getUserEntity().getUsername() + " has been deactivated");
    }

    public TraineeEntity createTrainee(TraineeEntity traineeEntity) {
        if (traineeEntity == null) throw new IllegalArgumentException("Argument trainee must not be null!");
        userService.createUser(traineeEntity.getUserEntity());
        String username = traineeEntity.getUserEntity().getUsername();
        log.info("Trainee with id " + traineeEntity.getTraineeId() + " and username " + username + " has been created");
        return traineeRepository.save(traineeEntity);
    }

    public void updateTrainee(TraineeEntity traineeEntity) throws AuthenticationException {
        if (traineeEntity == null) throw new IllegalArgumentException("Argument trainee must not be null!");
        if (!authenticate(traineeEntity.getUserEntity().getUsername(), traineeEntity.getUserEntity().getPassword())) throw new AuthenticationException("Wrong credentials");
        TraineeEntity traineeEntity1 = selectTrainee(traineeEntity.getUserEntity().getUsername());
        traineeEntity.setTraineeId(traineeEntity1.getTraineeId());
        traineeEntity.getUserEntity().setUserId(traineeEntity1.getTraineeId());
        traineeRepository.save(traineeEntity);
        log.info("Trainee with id " + traineeEntity.getTraineeId() + " has been updated");
    }

    public void deleteTrainee(String username) {
        userService.deleteByUsername(username);
        log.info("Trainee with username " + username + " has been deleted");
    }

    public void updateTrainersList(TraineeEntity traineeEntity, Set<TrainerEntity> trainerEntities) throws AuthenticationException {
        if (traineeEntity == null) throw new IllegalArgumentException("Argument trainee must not be null!");
        if (!authenticate(traineeEntity.getUserEntity().getUsername(), traineeEntity.getUserEntity().getPassword())) throw new AuthenticationException("Wrong credentials");
        traineeEntity.setTrainerEntities(trainerEntities);
        traineeRepository.save(traineeEntity);
        log.info("Updated " + traineeEntity.getUserEntity().getUsername() + "'s trainer list");
    }
}
