package com.dyns.persevero.domain.model;

import com.dyns.persevero.enums.MuscleGroupType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "muscle_groups")
public class MuscleGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(
            nullable = false,
            unique = true
    )
    @Enumerated(EnumType.STRING)
    private MuscleGroupType name = MuscleGroupType.NONE;

    @OneToMany(mappedBy = "muscleGroup")
    Set<Muscle> muscles;
}
