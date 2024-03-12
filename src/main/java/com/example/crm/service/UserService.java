package com.example.crm.service;

import com.example.crm.model.entities.UserEntity;
import com.example.crm.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void createUser(UserEntity userEntity) {
        String firstName = userEntity.getFirstName();
        String lastName = userEntity.getLastName();
        String username = firstName + "." + lastName;
        int n = userRepository.numberOfUsersWithSameUsername(username);
        userEntity.setUsername(username);
        if (n > 0) {
            userEntity.setUsername(username + "-" + n);
        }
        userEntity.setPassword(RandomStringUtils.random(10, true, true));
        userRepository.save(userEntity);
    }

    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }
}
