package com.example.crm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {

    private Long trainingId;

    private TraineeDto traineeEntity;

    private TrainerDto trainerEntity;

    private String trainingName;

    private TrainingTypeDto type;

    private LocalDate date;

    private Integer duration;

}