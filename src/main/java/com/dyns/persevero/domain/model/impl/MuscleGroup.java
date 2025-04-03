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
@Getter
@Setter
@Builder
@Entity
@Table(name = "muscle_groups")
public class MuscleGroup implements Model<UUID, MuscleGroupName> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(
            nullable = false,
            unique = true
    )
    @Enumerated(EnumType.STRING)
    @NotNull(message = "")
    private MuscleGroupName name;

    @OneToMany(
            cascade = {CascadeType.ALL},
            mappedBy = "muscleGroup",
            orphanRemoval = true
    )
    @JsonIgnore
    Set<Muscle> muscles;

    public void setMuscles(Set<Muscle> muscles) {
        muscles.forEach(this::addMuscle);
    }

    public void addMuscle(Muscle muscle) {
        muscles.add(muscle);
        muscle.setMuscleGroup(this);
    }

    public void removeMuscle(Muscle muscle) {
        muscles.remove(muscle);
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
                "name=" + name +
                ", id=" + id +
                '}';
    }

    @Override
    public Class<?> getNameClass() {
        return name.getClass();
    }
}
