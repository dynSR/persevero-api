package com.dyns.persevero.repository;

import com.dyns.persevero.domain.model.Muscle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MuscleRepository extends CrudRepository<Muscle, UUID> {
}
