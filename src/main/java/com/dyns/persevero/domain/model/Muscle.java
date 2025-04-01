package com.dyns.persevero.domain.model;

import com.dyns.persevero.enums.MuscleType;
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
@Table(name = "muscles")
public class Muscle implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(
            nullable = false,
            unique = true
    )
    @Enumerated(EnumType.STRING)
    private MuscleType name = MuscleType.NONE;

    @Column(
            length = 200,
            nullable = false
    )
    private String description = Strings.EMPTY;

    @ManyToOne(
            cascade = {CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(name = "muscle_group_id")
    private MuscleGroup muscleGroup;

    @ManyToMany(
            mappedBy = "muscles",
            cascade = {CascadeType.MERGE, CascadeType.REMOVE}
    )
    private Set<Exercise> exercises;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Muscle muscle = (Muscle) o;
        return name == muscle.name && Objects.equals(description, muscle.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

}
