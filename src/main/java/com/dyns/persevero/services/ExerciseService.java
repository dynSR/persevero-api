package com.dyns.persevero.services;

import com.dyns.persevero.domain.model.impl.Exercise;

import java.util.UUID;

public interface ExerciseService extends Service<Exercise, UUID> {

    Exercise findByName(String name);

    void addMuscle(UUID exerciseId, UUID muscleId);

    void removeMuscle(UUID exerciseId, UUID muscleId);

    void addMuscles(UUID uuid, Iterable<UUID> muscleIds);

    void removeMuscles(UUID uuid, Iterable<UUID> muscleIds);

}
