package com.example.crm.repositories;

import com.example.crm.model.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    void deleteByUsername(String username);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.username LIKE ?1%")
    int numberOfUsersWithSameUsername(String username);
}
