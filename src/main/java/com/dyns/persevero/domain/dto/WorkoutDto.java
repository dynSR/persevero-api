package com.dyns.persevero.domain.dto;

import com.dyns.persevero.enums.Visibility;
import com.dyns.persevero.enums.WorkoutStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class WorkoutDto {
    // TODO: Set validation messages

    @NotNull(message = "")
    private WorkoutStatus status;

    @NotNull(message = "")
    private Visibility visibility;

    @NotNull(message = "")
    @Min(value = 1, message = "{value}")
    private int duration;

    @NotNull(message = "")
    @NotBlank(message = "")
    private String shareCode;

}
