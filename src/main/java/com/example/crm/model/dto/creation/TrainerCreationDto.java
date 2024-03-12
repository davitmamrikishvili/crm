package com.example.crm.model.dto.creation;

import com.example.crm.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerCreationDto {

    private Long trainerId;

    private UserDto userEntity;

}
