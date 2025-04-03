package com.dyns.persevero.services;

import com.dyns.persevero.domain.model.impl.MuscleGroup;
import com.dyns.persevero.enums.MuscleGroupName;

import java.util.UUID;

public interface MuscleGroupService extends Service<MuscleGroup, UUID> {

    MuscleGroup findByName(MuscleGroupName name);
}
