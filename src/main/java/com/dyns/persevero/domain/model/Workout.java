package com.dyns.persevero.domain.model;

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
@Getter
@Setter
@Builder
@Entity
@Table(name = "workouts")
public class Workout implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkoutStatus status = WorkoutStatus.PLANNED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility = Visibility.PRIVATE;

    @Column(columnDefinition = "SMALLINT DEFAULT 1", nullable = false)
    private int duration = 0;

    @Column(
            name = "created_at",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            updatable = false,
            nullable = false
    )
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(
            name = "share_code",
            updatable = false,
            nullable = false,
            unique = true
    )
    private String shareCode = UUID.randomUUID().toString();

}
