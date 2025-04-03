package com.dyns.persevero.services;

import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.enums.MuscleName;

import java.util.UUID;

public interface MuscleService extends Service<Muscle, UUID> {

    Muscle findByName(MuscleName name);

    Iterable<Muscle> findAllByExerciseId(UUID exerciseId);
}
