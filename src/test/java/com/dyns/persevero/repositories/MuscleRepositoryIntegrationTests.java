package com.dyns.persevero.repositories;

import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.domain.model.impl.MuscleGroup;
import com.dyns.persevero.enums.MuscleGroupName;
import com.dyns.persevero.enums.MuscleName;
import com.dyns.persevero.fixtures.impl.ExerciseFixture;
import com.dyns.persevero.fixtures.impl.MuscleFixture;
import com.dyns.persevero.fixtures.impl.MuscleGroupFixture;
import com.dyns.persevero.repositories.impl.AbstractRepositoryIntegrationTests;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

@Slf4j
@Transactional
public class MuscleRepositoryIntegrationTests extends AbstractRepositoryIntegrationTests<
        MuscleRepository,
        Muscle,
        MuscleFixture,
        UUID,
        MuscleName
        > {

    @Autowired
    protected ExerciseRepository exerciseRepository;
    private final ExerciseFixture exerciseFixture = new ExerciseFixture();

    @Autowired
    protected MuscleGroupRepository muscleGroupRepository;
    private final MuscleGroupFixture muscleGroupFixture = new MuscleGroupFixture();

    @BeforeEach
    @Override
    public void setDependencies() {
        muscleGroupFixture.getMany().forEach(muscleGroupRepository::save);
        exerciseFixture.getMany().forEach(exerciseRepository::save);
        getFixture().getMany().forEach(underTest::save);
    }

    @Override
    protected MuscleFixture getFixture() {
        return fixture != null ? fixture : new MuscleFixture();
    }

    @Test
    @DisplayName("Should delete a muscle without deleting any associated exercise(s)")
    public void givenMuscleWithExercises_whenDeleted_thenIsDeletedButAssociatedExercisesAreStillPersisted() {
        // GIVEN
        underTest.findByName(MuscleName.FOREARMS).ifPresentOrElse(foundMuscle -> {
                    foundMuscle.setExercises(
                            Set.copyOf((Collection<? extends Exercise>) exerciseRepository.findAll())
                    );
                    underTest.save(foundMuscle);

                    // WHEN
                    underTest.delete(foundMuscle);

                    // THEN
                    assertThat(((Collection<Exercise>) exerciseRepository.findAll()).size())
                            .isEqualTo(ExerciseFixture.FIXTURES_AMOUNT);
                }, () -> fail(getFailureMessage())
        );
    }

    @Test
    @DisplayName("Should delete a muscle and remove its reference from any associated exercise(s)")
    public void givenMuscleWithExercises_whenDeleted_IsDeletedAndNoExerciseReferenceIt() {
        // GIVEN
        underTest.findByName(MuscleName.FOREARMS).ifPresentOrElse(foundMuscle -> {
                    foundMuscle.setExercises(
                            Set.copyOf((Collection<? extends Exercise>) exerciseRepository.findAll())
                    );
                    underTest.save(foundMuscle);

                    // WHEN
                    underTest.delete(foundMuscle);

                    // THEN
                    assertThat(((Collection<Exercise>) exerciseRepository.findAll())
                            .stream()
                            .flatMap(exercise -> exercise.getMuscles().stream())
                            .toList()
                            .contains(foundMuscle)
                    ).isFalse();
                }, () -> fail(getFailureMessage())
        );
    }

    @Test
    @DisplayName("Should delete a muscle without deleting associated muscle group")
    public void givenMuscleWithMuscleGroup_whenDeleted_thenIsDeletedButAssociatedMuscleGroupIsStillPersisted() {
        // GIVEN
        Muscle muscle = getFixture().getOne();
        muscleGroupRepository.findByName(MuscleGroupName.CORE).ifPresentOrElse(
                foundMuscleGroup -> {
                    muscle.setMuscleGroup(foundMuscleGroup);
                    Muscle savedMuscle = underTest.save(muscle);

                    // WHEN
                    underTest.delete(savedMuscle);

                    // THEN
                    assertThat(
                            ((Collection<MuscleGroup>) muscleGroupRepository.findAll()).contains(foundMuscleGroup)
                    ).isTrue();
                },
                () -> fail(getFailureMessage(MuscleGroup.class.toString()))
        );
    }

    @Test
    @DisplayName("Should delete a muscle and associated muscle group should not reference it")
    public void givenMuscleWithMuscleGroup_whenDeleted_thenIsDeletedButAssociatedMuscleGroupDoNotReferenceIt() {
        // GIVEN
        muscleGroupRepository.findByName(MuscleGroupName.CORE).ifPresentOrElse(
                foundMuscleGroup -> {
                    Muscle muscle = getFixture().getOne();
                    muscle.setMuscleGroup(foundMuscleGroup);
                    Muscle savedMuscle = underTest.save(muscle);

                    // WHEN
                    underTest.delete(savedMuscle);

                    // THEN
                    assertThat(foundMuscleGroup.getMuscles().contains(savedMuscle)).isFalse();
                },
                () -> fail(getFailureMessage(MuscleGroup.class.toString()))
        );
    }

    @ParameterizedTest
    @EnumSource(value = MuscleName.class)
    @DisplayName("Should retrieve a muscle by name")
    public void shouldRetrieveAMuscleByName(MuscleName name) {
        // WHEN
        underTest.findByName(name).ifPresentOrElse(
                foundMuscle -> {
                    // THEN
                    assertThat(foundMuscle.getName()).isEqualTo(name);
                    log.info("Muscle found : {}", foundMuscle);
                },
                () -> fail(getFailureMessage())
        );
    }

    @Test
    @DisplayName("Should retrieve all muscles associated to an exercise")
    public void givenExercisesWithMuscles_whenQueryingByExerciseId_thenReturnsAssociatedMuscles() {
        // GIVEN
        List<Exercise> savedExercises = List.copyOf(
                (Collection<? extends Exercise>) exerciseRepository.findAll()
        );
        underTest.findAll()
                .forEach(savedMuscle -> savedMuscle.setExercises(Set.copyOf(savedExercises)));

        String validExerciseName = "Exercise 0";
        exerciseRepository.findByName(validExerciseName).ifPresentOrElse(
                foundExercise -> {
                    // WHEN
                    List<Muscle> exerciseMuscles = List.copyOf(
                            (Collection<? extends Muscle>) underTest.findAllByExerciseId(foundExercise.getId())
                    );

                    // THEN
                    assertThat(foundExercise.getMuscles().containsAll(exerciseMuscles)).isTrue();
                },
                () -> fail(getFailureMessage(Exercise.class.toString()))
        );
    }
}
