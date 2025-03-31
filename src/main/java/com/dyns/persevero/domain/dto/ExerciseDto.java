package com.dyns.persevero.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Data
@Getter
@Setter
@Builder
public class ExerciseDto {
    // TODO: Set validation messages

    @NotNull(message = "")
    @NotBlank(message = "")
    @Size(max = 150, message = "{max}")
    private String name;

    private String description;

    @Min(value = 1, message = "{value}")
    private int sets;

    @Min(value = 1, message = "{value}")
    private int reps;

    @Range(min = 1, max = 999, message = "{min} {max}")
    private float weight;

}
