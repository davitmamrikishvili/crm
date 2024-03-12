package com.example.crm.mappers.impl;

import com.example.crm.mappers.Mapper;
import com.example.crm.model.dto.TrainerDto;
import com.example.crm.model.entities.TrainerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapperImpl implements Mapper<TrainerEntity, TrainerDto> {

    private ModelMapper modelMapper;

    public TrainerMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public TrainerDto mapTo(TrainerEntity trainerEntity) {
        return modelMapper.map(trainerEntity, TrainerDto.class);
    }

    @Override
    public TrainerEntity mapFrom(TrainerDto trainerDto) {
        return modelMapper.map(trainerDto, TrainerEntity.class);
    }
}