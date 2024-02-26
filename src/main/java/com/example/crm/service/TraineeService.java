package com.example.crm.service;

import com.example.crm.model.Trainee;
import com.example.crm.model.Trainer;
import com.example.crm.repositories.TraineeRepository;
import com.example.crm.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class TraineeService {

    private final UserRepository userRepository;
    private final TraineeRepository traineeRepository;

    @Autowired
    public TraineeService(UserRepository userRepository, TraineeRepository traineeRepository) {
        this.userRepository = userRepository;
        this.traineeRepository = traineeRepository;
    }

    public Trainee selectTrainee(String username) {
        Optional<Trainee> trainee = traineeRepository.findByUserUsername(username);
        if (trainee.isEmpty()) {
            throw new IllegalArgumentException("Trainee with username " + username + " does not exist!");
        } else {
            return trainee.get();
        }
    }

    public boolean authenticate(String username, String password) {
        Trainee trainee = selectTrainee(username);
        return trainee.getUser().getPassword().equals(password);
    }

    public void changePassword(Trainee trainee, String password) throws AuthenticationException {
        if (trainee == null) throw new IllegalArgumentException("Argument trainee must not be null!");
        if (!authenticate(trainee.getUser().getUsername(), trainee.getUser().getPassword())) throw new AuthenticationException("Wrong credentials");
        trainee.getUser().setPassword(password);
        traineeRepository.save(trainee);
        log.info("Trainee with id " + trainee.getTraineeId() + " and username " + trainee.getUser().getUsername() + " has changed password");
    }
    
    public void deactivate(Trainee trainee) throws AuthenticationException {
        if (trainee == null) throw new IllegalArgumentException("Argument trainee must not be null!");
        if (!authenticate(trainee.getUser().getUsername(), trainee.getUser().getPassword())) throw new AuthenticationException("Wrong credentials");
        trainee.getUser().setActive(false);
        traineeRepository.save(trainee);
        log.info("Trainee with id " + trainee.getTraineeId() + " and username " + trainee.getUser().getUsername() + " has been deactivated");
    }

    public void createTrainee(Trainee trainee) {
        if (trainee == null) throw new IllegalArgumentException("Argument trainee must not be null!");
        String username = trainee.getUser().getUsername();
        int n = userRepository.numberOfUsersWithSameUsername(username);
        trainee.getUser().setUsername(username + "-" + n);
        traineeRepository.save(trainee);
        log.info("Trainee with id " + trainee.getTraineeId() + " and username " + username + " has been created");
    }

    public void updateTrainee(Trainee trainee) throws AuthenticationException {
        if (trainee == null) throw new IllegalArgumentException("Argument trainee must not be null!");
        if (!authenticate(trainee.getUser().getUsername(), trainee.getUser().getPassword())) throw new AuthenticationException("Wrong credentials");
        traineeRepository.save(trainee);
        log.info("Trainee with id " + trainee.getTraineeId() + " has been updated");
    }

    public void deleteTrainee(String username) {
        userRepository.deleteByUsername(username);
        log.info("Trainee with username " + username + " has been deleted");
    }

    public void updateTrainersList(Trainee trainee, Set<Trainer> trainers) throws AuthenticationException {
        if (trainee == null) throw new IllegalArgumentException("Argument trainee must not be null!");
        if (!authenticate(trainee.getUser().getUsername(), trainee.getUser().getPassword())) throw new AuthenticationException("Wrong credentials");
        trainee.setTrainers(trainers);
        traineeRepository.save(trainee);
        log.info("Updated " + trainee.getUser().getUsername() + "'s trainer list");
    }
}
