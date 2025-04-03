package com.dyns.persevero.domain.model.impl;

import com.dyns.persevero.domain.model.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
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
public class Exercise implements Model<UUID, String> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(
            length = 150,
            nullable = false,
            unique = true
    )
    @NotNull(message = "")
    @NotBlank(message = "")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "SMALLINT DEFAULT 1", nullable = false)
    private int sets;

    @Column(columnDefinition = "SMALLINT DEFAULT 1", nullable = false)
    private int reps;

    @Column(columnDefinition = "DECIMAL(5,2) DEFAULT 1", nullable = false)
    private float weight;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "muscle_exercise",
            joinColumns = @JoinColumn(name = "muscle_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<Muscle> muscles;

    public void setMuscles(Set<Muscle> muscles) {
        muscles.forEach(this::addMuscle);
    }

    public void addMuscle(Muscle muscle) {
        muscles.add(muscle);
        muscle.getExercises().add(this);
    }

    public void removeMuscle(Muscle muscle) {
        muscles.remove(muscle);
        muscle.getExercises().remove(this);
    }

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

    @Override
    public String toString() {
        return "Exercise{" +
                "weight=" + weight +
                ", reps=" + reps +
                ", sets=" + sets +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public Class<?> getNameClass() {
        return name.getClass();
    }
}
