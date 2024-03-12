package com.example.crm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDto {

    private Long trainerId;

    private UserDto userEntity;

    private String specialization;

    private Set<TraineeDto> traineeEntities;

}
