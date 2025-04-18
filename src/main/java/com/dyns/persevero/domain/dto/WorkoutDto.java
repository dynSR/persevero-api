package com.dyns.persevero.domain.dto;

import com.dyns.persevero.enums.Visibility;
import com.dyns.persevero.enums.WorkoutStatus;
import com.dyns.persevero.validations.validators.EnumValidator;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDto {

    private UUID id;

    @EnumValidator(enumClass = WorkoutStatus.class)
    private WorkoutStatus status;

    @EnumValidator(enumClass = Visibility.class)
    private Visibility visibility;

    @Min(value = 1L)
    private int duration;

    private LocalDateTime createdAt;

    private String shareCode;

    private UserDto owner;

    private List<ExerciseDto> exercises;

    private void setId(UUID uuid) {
        id = uuid;
    }

    private void setCreatedAt(LocalDateTime dateTime) {
        createdAt = dateTime;
    }

}
