package com.example.crm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDto {

    private Long traineeId;

    private UserDto userEntity;

    private String address;

    private LocalDate dob;

    private Set<TrainerDto> trainerEntities;

}