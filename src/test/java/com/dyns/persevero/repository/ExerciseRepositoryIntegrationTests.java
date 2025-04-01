package com.dyns.persevero.repository;

import com.dyns.persevero.domain.model.Exercise;
import com.dyns.persevero.domain.model.Muscle;
import com.dyns.persevero.enums.MuscleType;
import com.dyns.persevero.fixture.ExerciseFixture;
import com.dyns.persevero.fixture.MuscleFixture;
import com.dyns.persevero.fixture.MuscleGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ExerciseRepositoryIntegrationTests {

    @Autowired
    protected ExerciseRepository underTest;
    private final ExerciseFixture exerciseFixture = new ExerciseFixture();

    @Autowired
    protected MuscleRepository muscleRepository;
    private final MuscleFixture muscleFixture = new MuscleFixture();

    @Autowired
    protected MuscleGroupRepository muscleGroupRepository;
    private final MuscleGroupFixture muscleGroupFixture = new MuscleGroupFixture();
    private final Set<Muscle> muscles = new HashSet<>();

    @BeforeEach
    public void init() {
        // Save all muscle groups in the test db.
        muscleGroupFixture.getAll()
                .forEach(mg -> muscleGroupRepository.save(mg));

        // Save all muscles in the test db assigning one random muscle group to each.
        muscleFixture.getAll(true)
                .forEach(m -> {
                    m.setMuscleGroup(muscleGroupFixture.getOne());
                    muscleRepository.save(m);
                    muscles.add(m);
                });

        // Keep only two muscles to make tests easier to debug.
        muscles.removeIf(
                m -> !m.getName().equals(MuscleType.BICEPS) && !m.getName().equals(MuscleType.TRICEPS)
        );
    }

    @Nested
    @DisplayName("Save Exercise")
    public class Save {

        @Test
        @DisplayName("Should save and retrieve persisted exercise")
        public void shouldSaveAndRetrievePersistedExercise() {
            Exercise exercise = exerciseFixture.getOne();
            Exercise savedExercise = underTest.save(exercise);

            assertThat(savedExercise).isEqualTo(exercise);
            assertThat(underTest.findById(savedExercise.getId())).isPresent();
        }

        @Test
        @DisplayName(
                "Should not save and throw data integration violation exception"
        )
        public void shouldNotSaveAndThrowDataPersistenceViolationException() {
            Exercise exercise = exerciseFixture.getOne();
            Exercise duplicate = exerciseFixture.getOne();

            exercise.setName("DUPLICATE");
            duplicate.setName("DUPLICATE");
            assertThat(exercise.getName()).isEqualTo(duplicate.getName());

            underTest.save(exercise);
            try {
                underTest.save(duplicate);
            } catch (DataIntegrityViolationException exception) {
                assertThat(underTest.findById(exercise.getId())).isPresent();
                assertThat(underTest.findById(duplicate.getId())).isNotPresent();
                assertThat(exception).isExactlyInstanceOf(DataIntegrityViolationException.class);
            }
        }

        @Test
        @DisplayName(
                "Should not save an exercise with a name equal to null"
        )
        public void shouldNotSaveAnExerciseWithNameEqualToNull() {
            Exercise exercise = exerciseFixture.getOne();
            exercise.setName(null);
            assertThat(exercise.getName()).isNull();

            try {
                underTest.save(exercise);
            } catch (DataIntegrityViolationException exception) {
                assertThat(underTest.findById(exercise.getId())).isNotPresent();
                assertThat(exception).isExactlyInstanceOf(DataIntegrityViolationException.class);
            }
        }
    }

    @Nested
    @DisplayName("Find all exercises")
    public class FindAll {

        @Test
        @DisplayName("Should retrieve all exercises")
        public void shouldRetrieveAllExercises() {
            exerciseFixture.getMany().forEach(
                    e -> {
                        Exercise savedExercise = underTest.save(e);
                    }
            );

            List<Exercise> exercises = StreamSupport
                    .stream(underTest.findAll().spliterator(), false)
                    .toList();

            assertThat(exercises.isEmpty()).isFalse();
            assertThat(exercises.size()).isEqualTo(ExerciseFixture.FIXTURES_AMOUNT);
        }

    }

    @Nested
    @DisplayName("Find one exercise")
    public class FindOne {

        private final List<Exercise> savedExercises = new ArrayList<>();

        @BeforeEach
        public void init() {
            exerciseFixture.getMany().forEach(
                    e -> {
                        e.setMuscles(muscles);
                        Exercise savedExercise = underTest.save(e);
                        savedExercises.add(savedExercise);
                    }
            );
        }

        @Test
        @DisplayName("Should retrieve one exercise when a valid id is provided")
        public void shouldRetrieveOneExerciseWhenValidIdIsProvided() {
            Exercise exercise = savedExercises.get(
                    new Random().nextInt(savedExercises.size() - 1)
            );

            Optional<Exercise> foundExercise = underTest.findById(exercise.getId());
            foundExercise.ifPresent(
                    value -> {
                        assertThat(underTest.findById(value.getId())).isNotNull();
                        assertThat(underTest.findById(value.getId())).isPresent();
                        assertThat(exercise.getId()).isEqualTo(foundExercise.get().getId());
                        assertThat(exercise.getName()).isEqualTo(foundExercise.get().getName());
                        assertThat(exercise.getSets()).isEqualTo(foundExercise.get().getSets());
                        assertThat(exercise.getReps()).isEqualTo(foundExercise.get().getReps());
                        assertThat(exercise.getWeight()).isEqualTo(foundExercise.get().getWeight());
                    }
            );
        }
    }

    @Nested
    @DisplayName("Update exercise")
    public class Update {

        private Exercise savedExercise;

        @BeforeEach
        public void init() {
            savedExercise = underTest.save(exerciseFixture.getOne());
        }

        @Test
        @DisplayName("Should update an existing exercise")
        public void shouldUpdateExercise() {
            savedExercise.setName("Updated Name");
            savedExercise.setDescription("New description");
            savedExercise.setSets(4);
            savedExercise.setReps(12);
            savedExercise.setWeight(12.0f);
            savedExercise.getMuscles().addAll(muscles);

            Exercise updatedExercise = underTest.save(savedExercise);

            assertThat(updatedExercise.getName()).isEqualTo("Updated Name");
            assertThat(updatedExercise.getDescription()).isEqualTo("New description");
            assertThat(updatedExercise.getSets()).isEqualTo(4);
            assertThat(updatedExercise.getReps()).isEqualTo(12);
            assertThat(updatedExercise.getWeight()).isEqualTo(12.0f);
            assertThat(savedExercise.getMuscles()).isEqualTo(updatedExercise.getMuscles());
        }

    }

    @Nested
    @DisplayName("Partial update exercise")
    public class PartialUpdate {
        private Exercise savedExercise;

        @BeforeEach
        public void init() {
            savedExercise = underTest.save(exerciseFixture.getOne());
        }

        @Test
        @DisplayName("Should partially update an exercise")
        public void shouldPartiallyUpdateExercise() {
            savedExercise.setReps(15);
            Exercise partiallyUpdatedExercise = underTest.save(savedExercise);

            assertThat(partiallyUpdatedExercise.getReps()).isEqualTo(15);
            assertThat(partiallyUpdatedExercise.getName()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Delete exercise")
    public class Delete {

        private Exercise savedExercise;

        @BeforeEach
        public void init() {
            savedExercise = underTest.save(exerciseFixture.getOne());
        }

        @Test
        @DisplayName("Should delete given exercise")
        public void shouldDeleteExercise() {
            underTest.delete(savedExercise);
            assertThat(underTest.findById(savedExercise.getId())).isEmpty();
        }

        @Test
        @DisplayName("Should not delete non-existing exercise")
        public void shouldNotDeleteNonExistingExercise() {
            UUID invalidUUID = UUID.randomUUID();
            assertThat(underTest.findById(invalidUUID)).isEmpty();
        }
    }

}
