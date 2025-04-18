package com.dyns.persevero.repositories;

import com.dyns.persevero.domain.model.impl.Exercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, UUID> {

    Optional<Exercise> findByName(String name);

}
