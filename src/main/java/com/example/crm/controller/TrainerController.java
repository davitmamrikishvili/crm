package com.example.crm.controller;

import com.example.crm.mappers.Mapper;
import com.example.crm.model.dto.TrainerDto;
import com.example.crm.model.entities.TrainerEntity;
import com.example.crm.service.TrainerService;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TrainerController {

    private TrainerService trainerService;

    private Mapper<TrainerEntity, TrainerDto> trainerMapper;

    public TrainerController(TrainerService trainerService, Mapper<TrainerEntity, TrainerDto> trainerMapper) {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
    }

    @PostMapping(path = "/trainers")
    public ResponseEntity<TrainerDto> createTrainer(@RequestBody TrainerDto trainerDto) {
        TrainerEntity trainerEntity = trainerMapper.mapFrom(trainerDto);
        TrainerEntity savedTrainerEntity = trainerService.createTrainer(trainerEntity);
        return new ResponseEntity<>(trainerMapper.mapTo(savedTrainerEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/trainers")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        if (trainerService.authenticate(username, password)) {
            return ResponseEntity.ok("Login Successful");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/trainers")
    public ResponseEntity<TrainerDto> changeLogin(@RequestBody TrainerDto trainerDto, @RequestParam String newPassword) {
        TrainerEntity trainerEntity = trainerMapper.mapFrom(trainerDto);
        try {
            trainerService.changePassword(trainerEntity, newPassword);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/trainers/{username}")
    public ResponseEntity<TrainerDto> getTrainer(@PathVariable("username") String username) {
        try {
            TrainerEntity trainerEntity = trainerService.selectTrainer(username);
            TrainerDto trainerDto = trainerMapper.mapTo(trainerEntity);
            return new ResponseEntity<>(trainerDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/trainers/update")
    public ResponseEntity<TrainerDto> updateTrainer(@RequestBody TrainerDto trainerDto) {
        TrainerEntity trainerEntity = trainerMapper.mapFrom(trainerDto);
        try {
            trainerService.updateTrainer(trainerEntity);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/trainers")
    public ResponseEntity<TrainerDto> toggle(@RequestBody TrainerDto trainerDto) {
        TrainerEntity trainerEntity = trainerMapper.mapFrom(trainerDto);
        try {
            trainerService.toggle(trainerEntity);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

