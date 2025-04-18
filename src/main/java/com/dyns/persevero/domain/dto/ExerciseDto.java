package com.dyns.persevero.domain.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseDto {

    private UUID id;

    @Size(max = 150)
    private String name;

    private String description;

    @Min(value = 1L)
    private Integer sets;

    @Min(value = 1L)
    private Integer reps;

    @DecimalMax(value = "999.99")
    @DecimalMin(value = "1.00")
    private BigDecimal weight;

    private Set<MuscleDto> muscles;

    private void setId(UUID uuid) {
        id = uuid;
    }

}
