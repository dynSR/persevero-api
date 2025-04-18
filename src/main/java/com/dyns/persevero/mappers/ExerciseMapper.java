package com.dyns.persevero.mappers;

import com.dyns.persevero.domain.dto.ExerciseDto;
import com.dyns.persevero.domain.model.impl.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        uses = MuscleMapper.class,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ExerciseMapper {

    ExerciseMapper INSTANCE = Mappers.getMapper(ExerciseMapper.class);

    @Mapping(source = "muscles", target = "muscles")
    ExerciseDto toDto(Exercise entity);

    @Mapping(source = "muscles", target = "muscles")
    Exercise toEntity(ExerciseDto dto);

}
