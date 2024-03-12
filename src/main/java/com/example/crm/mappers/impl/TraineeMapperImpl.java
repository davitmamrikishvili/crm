package com.example.crm.mappers.impl;

import com.example.crm.mappers.Mapper;
import com.example.crm.model.dto.TraineeDto;
import com.example.crm.model.entities.TraineeEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TraineeMapperImpl implements Mapper<TraineeEntity, TraineeDto> {

    private ModelMapper modelMapper;

    public TraineeMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public TraineeDto mapTo(TraineeEntity traineeEntity) {
        return modelMapper.map(traineeEntity, TraineeDto.class);
    }

    @Override
    public TraineeEntity mapFrom(TraineeDto traineeDto) {
        return modelMapper.map(traineeDto, TraineeEntity.class);
    }
}
