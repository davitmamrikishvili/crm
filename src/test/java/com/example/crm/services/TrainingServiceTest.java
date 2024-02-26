package com.example.crm.services;

import com.example.crm.repositories.TrainingRepository;
import com.example.crm.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;


    @Test
    void createTraining_shouldThrowException_whenTrainingIsNull() {
        assertThrows(IllegalArgumentException.class, () -> trainingService.createTraining(null));
    }

}