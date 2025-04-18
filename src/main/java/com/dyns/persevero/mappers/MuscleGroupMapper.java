package com.dyns.persevero.mappers;

import com.dyns.persevero.domain.dto.MuscleGroupDto;
import com.dyns.persevero.domain.model.impl.MuscleGroup;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MuscleGroupMapper {

    MuscleGroupMapper INSTANCE = Mappers.getMapper(MuscleGroupMapper.class);

    MuscleGroupDto toDto(MuscleGroup entity);

    MuscleGroup toEntity(MuscleGroupDto dto);

}
