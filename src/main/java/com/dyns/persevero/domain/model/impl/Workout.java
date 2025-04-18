package com.dyns.persevero.domain.model.impl;

import com.dyns.persevero.domain.model.Model;
import com.dyns.persevero.enums.WorkoutStatus;
import com.dyns.persevero.enums.Visibility;
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
@Builder
@Entity
@Table(name = "workouts")
public class Workout implements Model<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkoutStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    private int duration;

    @Column(
            name = "created_at",
            columnDefinition = "TIMESTAMP",
            updatable = false,
            nullable = false
    )
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(
            name = "share_code",
            updatable = false,
            nullable = false,
            unique = true
    )
    private String shareCode;

    private void setId(UUID uuid) {
        id = uuid;
    }

    private void setCreatedAt(LocalDateTime dateTime) {
        createdAt = dateTime;
    }

}
