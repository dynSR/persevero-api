package com.dyns.persevero.repository;

import com.dyns.persevero.domain.model.MuscleGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MuscleGroupRepository extends CrudRepository<MuscleGroup, UUID> {
}
