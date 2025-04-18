package com.dyns.persevero.repositories;

import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.enums.MuscleName;
import com.dyns.persevero.fixtures.impl.ExerciseFixture;
import com.dyns.persevero.fixtures.impl.MuscleFixture;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@Slf4j
@Transactional
public class ExerciseRepositoryIntegrationTests extends AbstractRepositoryIntegrationTests<
        ExerciseRepository,
        Exercise,
        ExerciseFixture,
        UUID
        > {

    @Autowired
    protected MuscleRepository muscleRepository;
    private final MuscleFixture muscleFixture = new MuscleFixture();

    @BeforeEach
    @Override
    protected void setDependencies() {
        saveMany();
        muscleFixture.getMany().forEach(muscleRepository::save);
    }

    @Override
    protected ExerciseFixture getFixture() {
        return fixture != null ? fixture : new ExerciseFixture();
    }

    @Test
    @DisplayName("Should delete an exercise without deleting any muscle(s)")
    public void givenExercise_whenDeletingExercise_thenIsDeletedAndAssociatedMuscledAreStillPersisted() {
        // GIVEN
        String validExerciseName = "Exercise 0";
        underTest.findByName(validExerciseName).ifPresentOrElse(
                foundExercise -> {
                    foundExercise.setMuscles(
                            Set.copyOf((Collection<? extends Muscle>) muscleRepository.findAll())
                    );
                    saveOne(foundExercise);

                    // WHEN
                    underTest.delete(foundExercise);

                    // THEN
                    assertThat(
                            ((Collection<Muscle>) muscleRepository.findAll()).size()
                    ).isEqualTo(muscleFixture.getCreatedAmount());
                },
                () -> fail(getFailureMessage())
        );
    }

    @Test
    @DisplayName("Should delete an exercise and remove its reference from associated muscle(s)")
    public void givenExercise_whenDeletingExercise_thenIsDeletedAndNoMuscledReferenceIt() {
        // GIVEN
        String validExerciseName = "Exercise 0";
        underTest.findByName(validExerciseName).ifPresentOrElse(
                foundExercise -> {
                    foundExercise.setMuscles(
                            Set.copyOf((Collection<? extends Muscle>) muscleRepository.findAll())
                    );
                    saveOne(foundExercise);

                    // WHEN
                    underTest.delete(foundExercise);

                    // THEN
                    assertThat(
                            ((Collection<Muscle>) muscleRepository.findAll()).stream()
                                    .flatMap(muscle -> muscle.getExercises().stream())
                                    .collect(Collectors.toSet())
                                    .contains(foundExercise)
                    ).isFalse();
                },
                () -> fail(getFailureMessage())
        );
    }

}
