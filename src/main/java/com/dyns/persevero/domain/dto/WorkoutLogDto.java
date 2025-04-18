package com.dyns.persevero.domain.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutLogDto {

    private UUID id;

    @Min(value = 1L)
    private float totalDuration;

    private LocalDateTime completedAt;

    private UserDto owner;

    private void setId(UUID uuid) {
        id = uuid;
    }

    private void completedAt(LocalDateTime dateTime) {
        completedAt = dateTime;
    }
}
