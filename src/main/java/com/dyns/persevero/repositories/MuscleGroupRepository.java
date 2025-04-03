package com.dyns.persevero.repositories;

import com.dyns.persevero.domain.model.impl.MuscleGroup;
import com.dyns.persevero.enums.MuscleGroupName;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface MuscleGroupRepository extends CrudRepository<MuscleGroup, UUID> {

    Optional<MuscleGroup> findByName(MuscleGroupName name);

}
