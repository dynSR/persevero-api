//package com.dyns.persevero.repositories;
//
//import com.dyns.persevero.domain.model.impl.Exercise;
//import com.dyns.persevero.domain.model.impl.Muscle;
//import com.dyns.persevero.domain.model.impl.MuscleGroup;
//import com.dyns.persevero.enums.MuscleGroupName;
//import com.dyns.persevero.fixtures.impl.ExerciseFixture;
//import com.dyns.persevero.fixtures.impl.MuscleFixture;
//import com.dyns.persevero.fixtures.impl.MuscleGroupFixture;
//import com.dyns.persevero.repositories.impl.AbstractRepositoryIntegrationTests;
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.*;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.fail;
//
//@Slf4j
//@Transactional
//public class ExerciseRepositoryIntegrationTests extends AbstractRepositoryIntegrationTests<
//        ExerciseRepository,
//        Exercise,
//        ExerciseFixture,
//        UUID,
//        String
//        > {
//
//    @Autowired
//    protected MuscleRepository muscleRepository;
//    private final MuscleFixture muscleFixture = new MuscleFixture();
//    private final Set<Muscle> muscleSet = new HashSet<>();
//
//    @Autowired
//    protected MuscleGroupRepository muscleGroupRepository;
//    private final MuscleGroupFixture muscleGroupFixture = new MuscleGroupFixture();
//    private MuscleGroup savedDefaultMuscleGroup;
//
//    @BeforeEach
//    @Override
//    public void init() {
//        super.init();
//
//        muscleGroupRepository.findByName(MuscleGroupName.NONE).ifPresentOrElse(
//                mg -> savedDefaultMuscleGroup = mg,
//                () -> fail("Expected default muscle group")
//        );
//
//        // Save all muscles into test db.
//        muscleFixture.getMany().forEach(
//                m -> {
//                    m.setMuscleGroup(savedDefaultMuscleGroup);
//                    Muscle savedMuscle = muscleRepository.save(m);
//                    muscleSet.add(savedMuscle);
//                }
//        );
//
//        getFixture().getMany().forEach(e -> {
//            muscleSet.stream().findAny().ifPresent(e::addMuscle);
//            underTest.save(e);
//        });
//    }
//
//    @Override
//    protected ExerciseFixture getFixture() {
//        return fixture != null ? fixture : new ExerciseFixture();
//    }
//
//    /**
//     * Delete test with cascade check :
//     * <p> - Given one persisted exercise
//     * <p> - When deleting an exercise
//     * <p> - Then asserts that this exercise cannot be retrieved anymore
//     * <p> - Then asserts that associated muscles are not removed in cascade
//     */
//    @Test
//    @DisplayName("Should delete an exercise but not the associated muscles")
//    public void shouldDeleteExerciseButNotAssociatedMuscles() {
//        Exercise exercise = getFixture().getOne();
//        exercise.setMuscles(muscleSet);
//
//        Exercise savedExercise = underTest.save(exercise);
//        assertThat(savedExercise.getMuscles().size()).isNotZero();
//        assertThat(savedExercise.getMuscles()).isEqualTo(exercise.getMuscles());
//
//        underTest.findById(savedExercise.getId()).ifPresentOrElse(
//                e -> {
//                    assertThat(e.getMuscles().containsAll(muscleSet)).isTrue();
//                    underTest.delete(e);
//                }, () -> fail("Was expecting to find an exercise")
//        );
//
//        Set<Muscle> musclesFromDb = Set.copyOf(
//                (Collection<? extends Muscle>) muscleRepository.findAll()
//        );
//        assertThat(musclesFromDb.containsAll(muscleSet)).isTrue();
//    }
//
//    /**
//     * Find by name test :
//     * <p> - Given persisted exercises
//     * <p> - When finding an exercise by name
//     * <p> - Then asserts that this exercise exists and is persisted
//     */
//    @ParameterizedTest
//    @ValueSource(strings = {"Exercise 1", "Exercise 2", "Exercise 3", "Exercise 4"})
//    @DisplayName("Should retrieve an exercise by name")
//    public void shouldRetrieveAnExerciseByName(String name) {
//        getFixture().getMany().forEach(underTest::save);
//
//        underTest.findByName(name).ifPresentOrElse(
//                e -> assertThat(e.getName()).isEqualTo(name),
//                () -> fail("Was expecting to find an exercise by name")
//        );
//    }
//
//    @Test
//    @DisplayName("Should retrieve all exercises associated to a muscle")
//    public void shouldRetrieveAllExercisesAssociatedToMuscle() {
//        getFixture().getMany().forEach(e -> {
//
//
//            muscleSet.stream().findAny().ifPresent(e::addMuscle);
//            underTest.save(e);
//        });
//
//        log.info("Muscles sub set : {}", muscleSet.stream().map(Muscle::getName).toList());
//        muscleSet.stream().findFirst().ifPresentOrElse(m -> {
//            log.info("Muscle query : {}", m.getName());
//            List<Exercise> foundExercises = List.copyOf(
//                    (Collection<? extends Exercise>) underTest.findAllByMuscleId(m.getId())
//            );
//            assertThat(foundExercises.size()).isNotZero();
//
//            foundExercises.forEach(
//                    e -> {
//                        log.info("Found exercise muscles : {}", e.getMuscles().stream().map(Muscle::getName).toList());
//                        assertThat(e.getMuscles().contains(m)).isTrue();
//                    }
//            );
//        }, () -> fail("Was expecting to find a muscle"));
//    }
//}
