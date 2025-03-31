package com.dyns.persevero.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "workout_logs")
public class WorkoutLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id= UUID.randomUUID();

    @Column(columnDefinition = "SMALLINT DEFAULT 1", nullable = false)
    private int totalDuration = 0;

    @Column(name = "completed_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime completedAt = LocalDateTime.now();

}
