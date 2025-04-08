package com.dyns.persevero.domain.model.impl;

import com.dyns.persevero.domain.model.Model;
import com.dyns.persevero.enums.MuscleName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.util.HashSet;
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
public class Muscle implements Model<UUID, MuscleName> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(
            nullable = false,
            unique = true
    )
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Muscle name cannot be null")
    private MuscleName name;

    @Column(length = 200)
    private String description;

    @ManyToOne
    @JoinColumn(
            name = "muscle_group_id", referencedColumnName = "id"
    )
    private MuscleGroup muscleGroup;

    public void setMuscleGroup(@Nullable MuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup;
        if (muscleGroup != null) muscleGroup.addMuscle(this);
    }

    @ManyToMany(
            mappedBy = "muscles",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<Exercise> exercises;

    public void setExercises(Set<Exercise> exercises) {
        exercises.forEach(this::addExercise);
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
        exercise.getMuscles().add(this);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
        exercise.getMuscles().remove(this);
    }

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

    @Override
    public String toString() {
        return "Muscle{" +
                "id=" + id +
                ", name=" + name +
                ", description='" + description + '\'' +
                ", muscleGroup=" + muscleGroup +
                '}';
    }

    @Override
    public Class<?> getNamePropertyClass() {
        return name.getClass();
    }

    @PreRemove
    public void onPreRemove() {
        if (muscleGroup != null) muscleGroup.removeMuscle(this);
        new HashSet<>(exercises).forEach(exercise -> exercise.removeMuscle(this));
    }
}
