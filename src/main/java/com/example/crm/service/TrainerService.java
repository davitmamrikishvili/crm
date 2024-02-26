package com.example.crm.service;

import com.example.crm.model.Trainer;
import com.example.crm.repositories.TrainerRepository;
import com.example.crm.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TrainerService {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerService(UserRepository userRepository, TrainerRepository trainerRepository) {
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
    }

    public Trainer selectTrainer(String username) {
        Optional<Trainer> trainer = trainerRepository.findByUserUsername(username);
        if (trainer.isEmpty()) {
            throw new IllegalArgumentException("Trainer with username " + username + " does not exist!");
        } else {
            return trainer.get();
        }
    }

    public boolean authenticate(String username, String password) {
        Trainer trainer = selectTrainer(username);
        return trainer.getUser().getPassword().equals(password);
    }

    public void changePassword(Trainer trainer, String password) throws AuthenticationException {
        if (trainer == null) throw new IllegalArgumentException("Argument trainer must not be null!");
        if (!authenticate(trainer.getUser().getUsername(), trainer.getUser().getPassword())) throw new AuthenticationException("Wrong credentials");
        trainer.getUser().setPassword(password);
        trainerRepository.save(trainer);
        log.info("Trainer with id " + trainer.getTrainerId() + " and username " + trainer.getUser().getUsername() + " has changed password");
    }

    public void deactivate(Trainer trainer) throws AuthenticationException {
        if (trainer == null) throw new IllegalArgumentException("Argument trainer must not be null!");
        if (!authenticate(trainer.getUser().getUsername(), trainer.getUser().getPassword())) throw new AuthenticationException("Wrong credentials");
        trainer.getUser().setActive(false);
        trainerRepository.save(trainer);
        log.info("Trainer with id " + trainer.getTrainerId() + " and username " + trainer.getUser().getUsername() + " has been deactivated");
    }

    public void createTrainer(Trainer trainer) {
        if (trainer == null) throw new IllegalArgumentException("Argument trainer must not be null!");
        String username = trainer.getUser().getUsername();
        int n = userRepository.numberOfUsersWithSameUsername(username);
        trainer.getUser().setUsername(username + "-" + n);
        trainerRepository.save(trainer);
        log.info("Trainer with id " + trainer.getTrainerId() + " and username " + username + " has been created");
    }

    public void updateTrainer(Trainer trainer) throws AuthenticationException {
        if (trainer == null) throw new IllegalArgumentException("Argument trainer must not be null!");
        if (!authenticate(trainer.getUser().getUsername(), trainer.getUser().getPassword())) throw new AuthenticationException("Wrong credentials");
        trainerRepository.save(trainer);
        log.info("Trainer with id " + trainer.getTrainerId() + " has been updated");
    }
}
