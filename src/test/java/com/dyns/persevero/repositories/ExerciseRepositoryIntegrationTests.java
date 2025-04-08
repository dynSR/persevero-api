package com.dyns.persevero.repositories;

import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.enums.MuscleName;
import com.dyns.persevero.fixtures.impl.ExerciseFixture;
import com.dyns.persevero.fixtures.impl.MuscleFixture;
import com.dyns.persevero.repositories.impl.AbstractRepositoryIntegrationTests;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

@Slf4j
@Transactional
public class ExerciseRepositoryIntegrationTests extends AbstractRepositoryIntegrationTests<
        ExerciseRepository,
        Exercise,
        ExerciseFixture,
        UUID,
        String
        > {

    @Autowired
    protected MuscleRepository muscleRepository;
    private final MuscleFixture muscleFixture = new MuscleFixture();

    @BeforeEach
    @Override
    public void setDependencies() {
        muscleFixture.getMany().forEach(muscleRepository::save);
        getFixture().getMany().forEach(underTest::save);
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
                    underTest.save(foundExercise);

                    // WHEN
                    underTest.delete(foundExercise);

                    // THEN
                    assertThat(
                            ((Collection<Muscle>) muscleRepository.findAll()).size()
                    ).isEqualTo(MuscleFixture.FIXTURES_AMOUNT);
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
                    underTest.save(foundExercise);

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

    @Test
    @DisplayName("Should retrieve a list of exercises associated to a muscle (by id)")
    public void givenMuscle_whenQueryingByMuscleId_thenReturnsAssociatedExercises() {
        // GIVEN
        muscleRepository.findByName(MuscleName.BICEPS).ifPresentOrElse(
                foundMuscle -> {
                    foundMuscle.setExercises(
                            Set.copyOf((Collection<? extends Exercise>) underTest.findAll())
                    );
                    muscleRepository.save(foundMuscle);

                    // WHEN
                    List<Exercise> exercisesFound = List.copyOf(
                            (Collection<? extends Exercise>) underTest.findAllByMuscleId(foundMuscle.getId())
                    );

                    // THEN
                    assertThat(exercisesFound.size()).isEqualTo(ExerciseFixture.FIXTURES_AMOUNT);
                },
                () -> fail(getFailureMessage(Muscle.class.toString()))
        );
    }
}
