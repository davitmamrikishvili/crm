package com.example.crm.repositories;

import com.example.crm.model.User;
import org.springframework.data.repository.CrudRepository;
import com.example.crm.model.Trainee;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends CrudRepository<Trainee, Long> {
    Optional<Trainee> findByUserUsername(String username);
}
