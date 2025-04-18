package com.dyns.persevero.domain.dto;

import com.dyns.persevero.enums.MuscleName;
import com.dyns.persevero.validations.validators.EnumValidator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuscleDto {

    private UUID id;

    @EnumValidator(enumClass = MuscleName.class)
    private MuscleName name;

    @Size(max = 200)
    private String description;

    private MuscleGroupDto muscleGroup;

    private Set<ExerciseDto> exercises;

    private void setId(UUID uuid) {
        id = uuid;
    }

}
