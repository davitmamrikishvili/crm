package com.example.crm.model.dto.creation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeCreationDto {

    private Long traineeId;

    private UserCreationDto userEntity;

}
