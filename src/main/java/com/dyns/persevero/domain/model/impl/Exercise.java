package com.dyns.persevero.domain.model.impl;

import com.dyns.persevero.domain.model.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "exercises")
public class Exercise implements Model<UUID> {

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
    @NotBlank
    @Size(max = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    @NotNull
    @Min(value = 1L)
    private Integer sets;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    @NotNull
    @Min(value = 1L)
    private Integer reps;

    @Column(columnDefinition = "DECIMAL(5,2)", nullable = false)
    @NotNull
    @DecimalMax(value = "999.99")
    @DecimalMin(value = "1.00")
    private BigDecimal weight;

    @ManyToMany(mappedBy = "exercises")
    @JsonIgnore
    private Set<Muscle> muscles;

    private void setId(UUID uuid) {
        id = uuid;
    }

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
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sets=" + sets +
                ", reps=" + reps +
                ", weight=" + weight +
                '}';
    }

    @PreRemove
    public void onPreRemove() {
        new HashSet<>(muscles).forEach(muscle -> muscle.removeExercise(this));
    }

}
