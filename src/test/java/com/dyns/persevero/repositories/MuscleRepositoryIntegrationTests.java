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

import java.util.*;

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
    public void init() {
        // DEPENDENCIES
        muscleGroupFixture.getMany().forEach(muscleGroupRepository::save);
        exerciseFixture.getMany().forEach(exerciseRepository::save);

        muscleGroupRepository.findByName(MuscleGroupName.NONE).ifPresentOrElse(
                defaultMG -> getFixture().getMany().forEach(muscle -> {
                            muscle.setMuscleGroup(defaultMG);
                            underTest.save(muscle);
                        }
                ), () -> fail(String.format(failureMessageTemplate, "default muscle group"))
        );
    }

    @Override
    protected MuscleFixture getFixture() {
        return fixture != null ? fixture : new MuscleFixture();
    }

    @Test
    @DisplayName("should retrieve all entities")
    @Override
    public void shouldRetrieveAllEntities() {
        List<Muscle> allMuscles = List.copyOf((Collection<? extends Muscle>) underTest.findAll());
        log.info("All muscles : {}", allMuscles);

        assertThat(allMuscles).isNotNull();
        assertThat(allMuscles.size()).isEqualTo(getFixture().getMany().size());
    }

    /**
     * Delete test with cascade check for exercises :
     * <p> - Given persisted muscles
     * <p> - When deleting a muscle
     * <p> - Then asserts that this muscle cannot be retrieved anymore
     * <p> - Then asserts that all exercises in the db that were associated with it do not reference it anymore
     */
    @Test
    @DisplayName("Should delete a muscle without delete cascading exercise(s)")
    public void shouldDeleteMuscle_AndAllAssociatedExercises_ShouldNotReferenceItAnymore() {
        // Find all saved exercises
        Set<Exercise> savedExercises = Set.copyOf((Collection<? extends Exercise>) exerciseRepository.findAll());

        // Save muscles with exercises assigned
        List<Muscle> savedMuscles = getFixture().getMany().stream()
                .peek(m -> m.setExercises(savedExercises))
                .map(underTest::save)
                .toList();

        // Grab one saved muscle from the db
        Muscle savedMuscle = savedMuscles.getFirst();
        log.info(savedMuscle.getName().toString());
        underTest.findById(savedMuscle.getId()).ifPresentOrElse(foundMuscle -> {
                    // Store all found muscle's associated exercise(s)
                    List<Exercise> foundMuscleExercises = List.copyOf(foundMuscle.getExercises());
                    log.info("{}", foundMuscleExercises.stream().map(Exercise::getName).toList());

                    // And for each muscle's exercise(s) remove the muscle reference
                    foundMuscleExercises.forEach(exercise -> {
                        if (!foundMuscle.getExercises().contains(exercise)) return;
                        exercise.removeMuscle(foundMuscle);
                    });

                    // Delete the muscle found and assert that it is not persisted anymore
                    underTest.delete(foundMuscle);
                    assertThat(underTest.findById(savedMuscle.getId())).isNotPresent();

                    // Then assert that the database still contains the exercise(s) that were in the deleted muscle
                    assertThat(((Collection<Exercise>) exerciseRepository.findAll())
                            .stream()
                            .flatMap(exercise -> exercise.getMuscles().stream())
                            .toList()
                            .contains(savedMuscle))
                            .isFalse();
                }, () -> fail(String.format(failureMessageTemplate, "muscle"))
        );
    }

    /**
     * Delete test with cascade check for muscle group :
     * <p> - Given one persisted muscle
     * <p> - When deleting a muscle
     * <p> - Then asserts that this muscle cannot be retrieved anymore
     * <p> - Then asserts that associated muscle group is not removed in cascade
     */
    @Test
    @DisplayName("Should delete a muscle but not the associated muscle group")
    public void shouldDeleteMuscleButNotTheAssociatedMuscleGroup() {
        Muscle muscle = getFixture().getOne();
        muscleGroupRepository.findByName(MuscleGroupName.CORE).ifPresentOrElse(
                muscle::setMuscleGroup,
                () -> fail(String.format(failureMessageTemplate, "muscle group"))
        );

        Muscle savedMuscle = underTest.save(muscle);
        underTest.findById(savedMuscle.getId()).ifPresentOrElse(
                foundMuscle -> {
                    MuscleGroup muscleGroup = savedMuscle.getMuscleGroup();
                    assertThat(foundMuscle.getMuscleGroup()).isEqualTo(muscleGroup);

                    underTest.delete(foundMuscle);

                    List<MuscleGroup> muscleGroups = List.copyOf(
                            (Collection<? extends MuscleGroup>) muscleGroupRepository.findAll()
                    );
                    assertThat(muscleGroups.contains(muscleGroup)).isTrue();
                },
                () -> fail(String.format(failureMessageTemplate, "muscle"))
        );
    }

    /**
     * Find by name test :
     * <p> - Given persisted muscle
     * <p> - When finding a muscle by name
     * <p> - Then asserts that this muscle exists and is persisted
     */
    @ParameterizedTest
    @EnumSource(value = MuscleName.class)
    @DisplayName("Should retrieve a muscle by name")
    public void shouldRetrieveAMuscleByName(MuscleName name) {
        underTest.findByName(name).ifPresentOrElse(
                m -> {
                    assertThat(m.getName()).isEqualTo(name);
                    log.info("MuscleGroup : {}", m.getMuscleGroup());
                },
                () -> fail(String.format(failureMessageTemplate, ""))
        );
    }

    /**
     * Find by exercise id :
     * <p> - Given persisted muscles
     * <p> - When finding a muscle by one exercise id
     * <p> - Then asserts that this muscles can be found
     * <p> - Or asserts that none can be found and returns []
     */
    @Test
    @DisplayName("Should retrieve all muscles associated to an exercise")
    public void shouldRetrieveAllMusclesAssociatedToAnExercise() {
        List<Exercise> exercises = List.copyOf((Collection<? extends Exercise>) exerciseRepository.findAll());

        // Save a set of muscle with a random exercise assigned.
        List<Muscle> muscles = getFixture().getMany().subList(0, 4)
                .stream()
                .peek(m -> {
                    int randomIndex = new Random().nextInt(exercises.size());
                    m.addExercise(exercises.stream().toList().get(randomIndex));
                    m.getExercises().forEach(
                            e -> log.info("Saved muscle exercises : {} / {}", m.getName(), e.getName())
                    );
                })
                .map(underTest::save)
                .toList();

        // Grab one exercise and try to find all muscles associated with it
        Exercise savedExercise = exercises.getFirst();
        exerciseRepository.findById(savedExercise.getId()).ifPresentOrElse(foundExercise -> {
                    List<Muscle> foundMuscles = List.copyOf(
                            (Collection<? extends Muscle>) underTest.findAllByExerciseId(foundExercise.getId())
                    );
                    log.info("Found muscles: {}", foundMuscles.stream().map(Muscle::getName).toList());

                    List<UUID> expectedIds = muscles.stream()
                            .filter(m -> m.getExercises().contains(foundExercise))
                            .map(Muscle::getId)
                            .toList();
                    log.info("Expected ids : {}", expectedIds);
                    List<UUID> actualIds = foundMuscles.stream().map(Muscle::getId).toList();
                    log.info("Actual ids : {}", actualIds);

                    // These assertions here expect either to contain all expected Ids
                    // Or none of them as none of the muscles contain the exercise grabbed above
                    if (!expectedIds.isEmpty()) assertThat(actualIds.containsAll(expectedIds)).isTrue();
                    else assertThat(actualIds.isEmpty()).isTrue();
                },
                () -> fail(String.format(failureMessageTemplate, "exercise"))
        );
    }
}
