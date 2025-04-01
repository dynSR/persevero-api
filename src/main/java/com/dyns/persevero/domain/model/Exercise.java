package com.dyns.persevero.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.logging.log4j.util.Strings;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "exercises")
public class Exercise implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(
            length = 150,
            nullable = false,
            unique = true
    )
    private String name = Strings.EMPTY;

    @Column(columnDefinition = "TEXT")
    private String description = Strings.EMPTY;

    @Column(columnDefinition = "SMALLINT DEFAULT 1", nullable = false)
    private int sets = 1;

    @Column(columnDefinition = "SMALLINT DEFAULT 1", nullable = false)
    private int reps = 1;

    @Column(columnDefinition = "DECIMAL(5,2) DEFAULT 1", nullable = false)
    private float weight = 1.0f;

    @ManyToMany
    @JoinTable(
            name = "exercise_muscle_group",
            joinColumns = @JoinColumn(name = "muscle_group_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private Set<Muscle> muscles;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(name, exercise.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
