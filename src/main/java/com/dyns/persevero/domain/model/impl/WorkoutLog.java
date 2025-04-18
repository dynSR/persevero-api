package com.dyns.persevero.domain.model.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "workout_logs")
public class WorkoutLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    @Min(value = 1L)
    private int totalDuration;

    @Column(name = "completed_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime completedAt;

    private void setId(UUID uuid) {
        id = uuid;
    }

    private void setCompletedAt(LocalDateTime dateTime) {
        completedAt = dateTime;
    }

}
