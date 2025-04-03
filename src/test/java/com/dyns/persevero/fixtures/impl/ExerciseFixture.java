package com.dyns.persevero.fixtures.impl;


import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.fixtures.Fixture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class ExerciseFixture implements Fixture<Exercise> {

    public final static int FIXTURES_AMOUNT = 8;
    private final List<Exercise> exercises = new ArrayList<>();

    public Exercise getOne() {
        int randomIndex = new Random().nextInt(FIXTURES_AMOUNT - 1);
        return getMany().get(randomIndex);
    }

    public List<Exercise> getMany() {
        for (int i = 0; i < FIXTURES_AMOUNT; i++) {
            exercises.add(
                    Exercise.builder()
                            .name("Exercise " + i)
                            .description("Exercise description")
                            .sets(2)
                            .reps(4)
                            .weight(12.5f)
                            .muscles(new HashSet<>())
                            .build()
            );
        }
        return exercises;
    }

}
