package com.example.crm.repositories;

import com.example.crm.model.Trainer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends CrudRepository<Trainer, Long> {
    Optional<Trainer> findByUserUsername(String username);
}
