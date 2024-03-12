package com.example.crm.repositories;

import com.example.crm.model.entities.TrainingTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends CrudRepository<TrainingTypeEntity, Long> {
}
