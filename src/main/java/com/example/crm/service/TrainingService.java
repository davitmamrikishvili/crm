package com.example.crm.service;

import com.example.crm.model.Trainee;
import com.example.crm.model.Trainer;
import com.example.crm.model.Training;
import com.example.crm.model.TrainingType;
import com.example.crm.repositories.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final EntityManager entityManager;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository, EntityManager entityManager) {
        this.trainingRepository = trainingRepository;
        this.entityManager = entityManager;
    }

    public void createTraining(Training training) {
        if (training == null) throw new IllegalArgumentException("Argument training must not be null!");
        trainingRepository.save(training);
        log.info("Training with id " + training.getTrainingId() + " has been created");
    }

    public List<Training> getTraineeTrainingList(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingTypeName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> trainingRoot = query.from(Training.class);
        Join<Training, Trainee> traineeJoin = trainingRoot.join("trainee");
        Join<Training, Trainer> trainerJoin = trainingRoot.join("trainer");
        Join<Training, TrainingType> typeJoin = trainingRoot.join("type");

        List<Predicate> predicates = helper(traineeUsername, fromDate, toDate, trainerName, cb, trainingRoot, traineeJoin.get("user"), trainerJoin.get("user"));
        if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
            predicates.add(cb.like(typeJoin.get("typeName"), "%" + trainingTypeName + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Training> getTrainerTrainingList(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> trainingRoot = query.from(Training.class);
        Join<Training, Trainer> trainerJoin = trainingRoot.join("trainer");
        Join<Training, Trainee> traineeJoin = trainingRoot.join("trainee");

        List<Predicate> predicates = helper(trainerUsername, fromDate, toDate, traineeName, cb, trainingRoot, trainerJoin.get("user"), traineeJoin.get("user"));


        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

    private List<Predicate> helper(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName, CriteriaBuilder cb, Root<Training> trainingRoot, Path<Object> user, Path<Object> user2) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(user.get("username"), trainerUsername));

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(trainingRoot.get("date"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(trainingRoot.get("date"), toDate));
        }
        if (traineeName != null && !traineeName.isEmpty()) {
            predicates.add(cb.like(user2.get("username"), "%" + traineeName + "%"));
        }
        return predicates;
    }
}
