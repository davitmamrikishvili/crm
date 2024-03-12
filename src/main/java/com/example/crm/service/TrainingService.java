package com.example.crm.service;

import com.example.crm.model.entities.TraineeEntity;
import com.example.crm.model.entities.TrainerEntity;
import com.example.crm.model.entities.TrainingEntity;
import com.example.crm.model.entities.TrainingTypeEntity;
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

    public void createTraining(TrainingEntity trainingEntity) {
        if (trainingEntity == null) throw new IllegalArgumentException("Argument training must not be null!");
        trainingRepository.save(trainingEntity);
        log.info("Training with id " + trainingEntity.getTrainingId() + " has been created");
    }

    public List<TrainingEntity> getTraineeTrainingList(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingTypeName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> query = cb.createQuery(TrainingEntity.class);
        Root<TrainingEntity> trainingRoot = query.from(TrainingEntity.class);
        Join<TrainingEntity, TraineeEntity> traineeJoin = trainingRoot.join("trainee");
        Join<TrainingEntity, TrainerEntity> trainerJoin = trainingRoot.join("trainer");
        Join<TrainingEntity, TrainingTypeEntity> typeJoin = trainingRoot.join("type");

        List<Predicate> predicates = helper(traineeUsername, fromDate, toDate, trainerName, cb, trainingRoot, traineeJoin.get("user"), trainerJoin.get("user"));
        if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
            predicates.add(cb.like(typeJoin.get("typeName"), "%" + trainingTypeName + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

    public List<TrainingEntity> getTrainerTrainingList(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> query = cb.createQuery(TrainingEntity.class);
        Root<TrainingEntity> trainingRoot = query.from(TrainingEntity.class);
        Join<TrainingEntity, TrainerEntity> trainerJoin = trainingRoot.join("trainer");
        Join<TrainingEntity, TraineeEntity> traineeJoin = trainingRoot.join("trainee");

        List<Predicate> predicates = helper(trainerUsername, fromDate, toDate, traineeName, cb, trainingRoot, trainerJoin.get("user"), traineeJoin.get("user"));


        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

    private List<Predicate> helper(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName, CriteriaBuilder cb, Root<TrainingEntity> trainingRoot, Path<Object> user, Path<Object> user2) {
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
