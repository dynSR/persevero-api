package com.dyns.persevero.repository;

import com.dyns.persevero.domain.model.Exercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExerciseRepository extends CrudRepository <Exercise, UUID> {
}
