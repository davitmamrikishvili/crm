package com.example.crm.repositories;

import com.example.crm.model.entities.TraineeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends CrudRepository<TraineeEntity, Long> {
    Optional<TraineeEntity> findByUserEntityUsername(String username);
}
