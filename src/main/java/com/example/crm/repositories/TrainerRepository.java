package com.example.crm.repositories;

import com.example.crm.model.entities.TrainerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends CrudRepository<TrainerEntity, Long> {
    Optional<TrainerEntity> findByUserEntityUsername(String username);
}
