package com.example.crm.controller;

import com.example.crm.mappers.Mapper;
import com.example.crm.model.dto.TraineeDto;
import com.example.crm.model.entities.TraineeEntity;
import com.example.crm.service.TraineeService;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TraineeController {

    private TraineeService traineeService;

    private Mapper<TraineeEntity, TraineeDto> traineeMapper;

    public TraineeController(TraineeService traineeService, Mapper<TraineeEntity, TraineeDto> traineeMapper) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
    }

    @PostMapping(path = "/trainees")
    public ResponseEntity<TraineeDto> createTrainee(@RequestBody TraineeDto traineeDto) {
        TraineeEntity traineeEntity = traineeMapper.mapFrom(traineeDto);
        TraineeEntity savedTraineeEntity = traineeService.createTrainee(traineeEntity);
        return new ResponseEntity<>(traineeMapper.mapTo(savedTraineeEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/trainees")
    public ResponseEntity<TraineeDto> login(@RequestParam String username, @RequestParam String password) {
        if (traineeService.authenticate(username, password)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "/trainees")
    public ResponseEntity<TraineeDto> changeLogin(@RequestBody TraineeDto traineeDto, @RequestParam String newPassword) {
        TraineeEntity traineeEntity = traineeMapper.mapFrom(traineeDto);
        try {
            traineeService.changePassword(traineeEntity, newPassword);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/trainees/{username}")
    public ResponseEntity<TraineeDto> getTrainee(@PathVariable("username") String username) {
        try {
            TraineeEntity traineeEntity = traineeService.selectTrainee(username);
            TraineeDto traineeDto = traineeMapper.mapTo(traineeEntity);
            return new ResponseEntity<>(traineeDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/trainees/update")
    public ResponseEntity<TraineeDto> updateTrainee(@RequestBody TraineeDto traineeDto) {
        TraineeEntity traineeEntity = traineeMapper.mapFrom(traineeDto);
        try {
            traineeService.updateTrainee(traineeEntity);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/trainees/{username}")
    public ResponseEntity<TraineeDto> deleteTrainee(@PathVariable("username") String username) {
        traineeService.deleteTrainee(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/trainees")
    public ResponseEntity<TraineeDto> toggle(@RequestBody TraineeDto traineeDto) {
        TraineeEntity traineeEntity = traineeMapper.mapFrom(traineeDto);
        try {
            traineeService.toggle(traineeEntity);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
