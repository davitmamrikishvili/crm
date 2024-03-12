package com.example.crm.repositories;

import com.example.crm.model.entities.TrainingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends CrudRepository<TrainingEntity, Long> {
}
