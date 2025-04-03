package com.dyns.persevero.fixtures.impl;

import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.enums.MuscleName;
import com.dyns.persevero.fixtures.Fixture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MuscleFixture implements Fixture<Muscle> {

    public final static int FIXTURES_AMOUNT = MuscleName.values().length;
    private final List<Muscle> muscles = new ArrayList<>();

    public Muscle getOne() {
        int randomIndex = new Random().nextInt(FIXTURES_AMOUNT - 1);
        return getMany().get(randomIndex);
    }

    public List<Muscle> getMany() {
        for (MuscleName name : MuscleName.values()) {
            muscles.add(Muscle.builder()
                    .id(null)
                    .name(name)
                    .description("Muscle description")
                    .exercises(new HashSet<>())
                    .build()
            );
        }

        return muscles;
    }
}
