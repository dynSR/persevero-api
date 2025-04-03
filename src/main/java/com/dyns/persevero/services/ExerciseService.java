package com.dyns.persevero.services;

import com.dyns.persevero.domain.model.impl.Exercise;

import java.util.UUID;

public interface ExerciseService extends Service<Exercise, UUID> {

    Exercise findByName(String name);

    Iterable<Exercise> findAllByMuscleId(UUID muscleId);

}
