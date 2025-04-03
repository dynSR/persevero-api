package com.dyns.persevero.repositories;

import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.enums.MuscleName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MuscleRepository extends CrudRepository<Muscle, UUID> {

    Optional<Muscle> findByName(MuscleName name);

    @Query("SELECT m FROM Muscle m JOIN m.exercises e WHERE e.id = :exerciseId")
    Iterable<Muscle> findAllByExerciseId(UUID exerciseId);

}
