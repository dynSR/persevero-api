package com.dyns.persevero.domain.model.impl;

import com.dyns.persevero.domain.model.Model;
import com.dyns.persevero.enums.MuscleGroupName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "muscle_groups")
public class MuscleGroup implements Model<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(
            nullable = false,
            unique = true
    )
    @Enumerated(EnumType.STRING)
    @NotNull
    private MuscleGroupName name;

    @OneToMany(mappedBy = "muscleGroup")
    @JsonIgnore
    Set<Muscle> muscles;

    private void setId(UUID uuid) {
        id = uuid;
    }

    public void setMuscles(Set<Muscle> muscles) {
        muscles.forEach(this::addMuscle);
    }

    public void addMuscle(Muscle muscle) {
        muscles.add(muscle);
        if (muscle.getMuscleGroup() != this) muscle.setMuscleGroup(this);
    }

    public void removeMuscle(Muscle muscle) {
        muscles.remove(muscle);
        muscle.setMuscleGroup(null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MuscleGroup that = (MuscleGroup) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "MuscleGroup{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

    @PreRemove
    public void onPreRemove() {
        muscles.forEach(muscle -> muscle.setMuscleGroup(null));
    }

}
