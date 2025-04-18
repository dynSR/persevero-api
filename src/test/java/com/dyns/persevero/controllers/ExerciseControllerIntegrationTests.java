package com.dyns.persevero.controllers;

import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.fixtures.impl.ExerciseFixture;
import com.dyns.persevero.fixtures.impl.MuscleFixture;
import com.dyns.persevero.services.ExerciseService;
import com.dyns.persevero.services.MuscleService;
import com.dyns.persevero.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Transactional
public class ExerciseControllerIntegrationTests {

    private final ExerciseService exerciseService;
    private final ExerciseFixture fixture = new ExerciseFixture();

    private final MuscleService muscleService;
    private final MuscleFixture muscleFixture = new MuscleFixture();

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private final static String API_EXERCISES_URI = Constants.API_URL_PREFIX + "/exercises/";

    @Autowired
    public ExerciseControllerIntegrationTests(
            final ExerciseService exerciseService,
            final MuscleService muscleService,
            final MockMvc mockMvc,
            final ObjectMapper objectMapper
    ) {
        this.exerciseService = exerciseService;
        this.muscleService = muscleService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    protected Exercise getSaveEntity() {
        return getSaveEntity(fixture.getOne());
    }

    protected Exercise getSaveEntity(Exercise exercise) {
        return exerciseService.save(exercise);
    }

    protected void saveMany() {
        fixture.getMany().forEach(exerciseService::save);
    }

    @Test
    @DisplayName("Should return a list of exercises with http status 200")
    public void givenExercises_whenFetchingAllSucceeds_thenReturnsListOfExercise() throws Exception {
        // GIVEN
        saveMany();

        // WHEN + THEN
        mockMvc.perform(
                        get("/api/v1/exercises")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$[0].id").isString()
                )
                .andExpect(
                        jsonPath("$[0].name").value("Exercise 0")
                )
                .andExpect(
                        jsonPath("$[0].description")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_DESCRIPTION)
                )
                .andExpect(
                        jsonPath("$[0].sets")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_SETS_VALUE)
                )
                .andExpect(
                        jsonPath("$[0].reps")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_REPS_VALUE)
                )
                .andExpect(
                        jsonPath("$[0].weight")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_WEIGHT_VALUE)
                )
                .andReturn();
    }

    @Test
    @DisplayName("Should return one exercise with http status 200")
    public void givenExercise_whenFetchingOneSucceeds_thenReturnsExercise() throws Exception {
        // GIVEN
        Exercise existingExercise = getSaveEntity(fixture.getOne());

        // WHEN + THEN
        mockMvc.perform(
                        get(API_EXERCISES_URI + existingExercise.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.id").isString()
                )
                .andExpect(
                        jsonPath("$.name").value("Exercise 0")
                )
                .andExpect(
                        jsonPath("$.description")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_DESCRIPTION)
                )
                .andExpect(
                        jsonPath("$.sets")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_SETS_VALUE)
                )
                .andExpect(
                        jsonPath("$.reps")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_REPS_VALUE)
                )
                .andExpect(
                        jsonPath("$.weight")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_WEIGHT_VALUE)
                )
                .andReturn();
    }

    @Test
    @DisplayName("Should create and return an exercise with http status 201")
    public void givenExercise_whenSavingSucceeds_thenReturnsCreatedExercise() throws Exception {
        // GIVEN
        Exercise exerciseToCreate = fixture.getOne();
        String exerciseJson = objectMapper.writeValueAsString(exerciseToCreate);

        // WHEN + THEN
        mockMvc.perform(
                        post("/api/v1/exercises")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(exerciseJson)
                ).andDo(print())
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        jsonPath("$.id").isString()
                )
                .andExpect(
                        jsonPath("$.name").value("Exercise 0")
                )
                .andExpect(
                        jsonPath("$.description")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_DESCRIPTION)
                )
                .andExpect(
                        jsonPath("$.sets")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_SETS_VALUE)
                )
                .andExpect(
                        jsonPath("$.reps")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_REPS_VALUE)
                )
                .andExpect(
                        jsonPath("$.weight")
                                .value(ExerciseFixture.EXERCISE_FIXTURE_DEFAULT_WEIGHT_VALUE)
                )
                .andReturn();
    }

    @Test
    @DisplayName("Should fully update exercise and return it with http status 200")
    public void givenExercise_whenFullUpdatingSucceeds_thenReturnsUpdatedExercise() throws Exception {
        // GIVEN
        Exercise existingExercise = getSaveEntity(fixture.getOne());
        String updatedName = "Exercise A";
        String updatedDescription = "This is an updated exercise";
        Integer updatedSets = 7;
        Integer updatedReps = 25;
        BigDecimal updatedWeight = BigDecimal.valueOf(250.95);
        existingExercise.setName(updatedName);
        existingExercise.setDescription(updatedDescription);
        existingExercise.setSets(updatedSets);
        existingExercise.setReps(updatedReps);
        existingExercise.setWeight(updatedWeight);

        // WHEN + THEN
        mockMvc.perform(
                        put(API_EXERCISES_URI + existingExercise.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(existingExercise))
                ).andDo(print())
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.name").value(updatedName)
                )
                .andExpect(
                        jsonPath("$.description").value(updatedDescription)
                )
                .andExpect(
                        jsonPath("$.sets").value(updatedSets)
                )
                .andExpect(
                        jsonPath("$.reps").value(updatedReps)
                )
                .andExpect(
                        jsonPath("$.weight").value(updatedWeight)
                )
                .andReturn();
    }

    @Test
    @DisplayName("Should only update exercise name and return it with http status 200")
    public void givenExerciseAndNewName_whenPartiallyUpdatingSucceeds_thenReturnsUpdatedExercise() throws Exception {
        // GIVEN
        Exercise existingExercise = getSaveEntity(fixture.getOne());
        String partialUpdateJson = """
                {
                    "name": "Updated exercise"
                }
                """;

        // WHEN + THEN
        mockMvc.perform(
                        patch(API_EXERCISES_URI + existingExercise.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(partialUpdateJson)
                ).andDo(print())
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.name").value("Updated exercise")
                )
                .andReturn();
    }

    @Test
    @DisplayName("Should delete exercise and return no content with http status 204")
    public void givenExercise_whenDeletingSucceeds_thenReturnsNoContent() throws Exception {
        // GIVEN
        Exercise existingExercise = getSaveEntity(fixture.getOne());

        // WHEN + THEN
        mockMvc.perform(
                        delete(API_EXERCISES_URI + existingExercise.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(
                        status().isNoContent()
                )
                .andReturn();
    }

    @Test
    @DisplayName("Should add multiple muscles to an exercise")
    void givenMusclesToExercise_whenSavedSucceeds_ThenAddsAllMusclesAndReturnsNoContent() throws Exception {
        // GIVEN
        Set<Muscle> savedMuscles = muscleFixture.getMany()
                .stream()
                .peek(muscleService::save)
                .collect(Collectors.toSet());
        Exercise existingExercise = getSaveEntity();
        Set<UUID> muscleIds = Set.copyOf(
                savedMuscles.stream()
                        .map(Muscle::getId)
                        .collect(Collectors.toSet())
        );

        // WHEN + THEN
        mockMvc.perform(
                        post(API_EXERCISES_URI + existingExercise.getId() + "/muscles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(muscleIds))
                ).andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return http status 404")
    public void givenInvalidId_whenFetchingOneFails_thenReturnsHttpStatusNotFound() throws Exception {
        // GIVEN
        UUID invalidId = UUID.randomUUID();

        // WHEN + THEN
        mockMvc.perform(
                        get(API_EXERCISES_URI + invalidId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(
                        status().isNotFound()
                )
                .andReturn();
    }

}
