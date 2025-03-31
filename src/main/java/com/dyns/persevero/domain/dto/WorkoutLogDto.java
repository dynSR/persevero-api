package com.dyns.persevero.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Data
@Getter
@Setter
@Builder
public class WorkoutLogDto {
    // TODO: Set validation messages

    @NotNull(message = "")
    @Min(value = 1, message = "{value}")
    private float totalDuration;
}
