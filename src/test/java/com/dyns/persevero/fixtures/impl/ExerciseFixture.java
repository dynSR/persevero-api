package com.dyns.persevero.fixtures.impl;


import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.fixtures.Fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ExerciseFixture implements Fixture<Exercise> {

    private final static int EXERCISE_FIXTURE_AMOUNT = 8;
    public final static String EXERCISE_FIXTURE_DEFAULT_DESCRIPTION = "Exercise description";
    public final static int EXERCISE_FIXTURE_DEFAULT_SETS_VALUE = 2;
    public final static int EXERCISE_FIXTURE_DEFAULT_REPS_VALUE = 4;
    public final static BigDecimal EXERCISE_FIXTURE_DEFAULT_WEIGHT_VALUE = BigDecimal.valueOf(12.50);

    private final List<Exercise> exercises = new ArrayList<>();

    public Exercise getOne() {
        return getMany().getFirst();
    }

    public List<Exercise> getMany() {
        for (int i = 0; i < EXERCISE_FIXTURE_AMOUNT; i++) {
            exercises.add(
                    Exercise.builder()
                            .name("Exercise " + i)
                            .description(EXERCISE_FIXTURE_DEFAULT_DESCRIPTION)
                            .sets(EXERCISE_FIXTURE_DEFAULT_SETS_VALUE)
                            .reps(EXERCISE_FIXTURE_DEFAULT_REPS_VALUE)
                            .weight(EXERCISE_FIXTURE_DEFAULT_WEIGHT_VALUE)
                            .muscles(new HashSet<>())
                            .build()
            );
        }
        return exercises;
    }

    public long getCreatedAmount() {
        return EXERCISE_FIXTURE_AMOUNT;
    }

}
