package com.example.crm.repositories;

import com.example.crm.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    void deleteByUsername(String username);

    @Query("SELECT COUNT(u) FROM User u WHERE u.username LIKE ?1")
    int numberOfUsersWithSameUsername(String username);
}
