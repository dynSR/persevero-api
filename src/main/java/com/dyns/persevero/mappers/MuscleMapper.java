package com.dyns.persevero.mappers;

import com.dyns.persevero.domain.dto.MuscleDto;
import com.dyns.persevero.domain.model.impl.Muscle;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MuscleMapper {

    MuscleMapper INSTANCE = Mappers.getMapper(MuscleMapper.class);

    MuscleDto toDto(Muscle entity);

    Muscle toEntity(MuscleDto dto);

}
