package com.dyns.persevero.fixtures.impl;

import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.enums.MuscleName;
import com.dyns.persevero.fixtures.Fixture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MuscleFixture implements Fixture<Muscle> {

    private final List<Muscle> muscles = new ArrayList<>();

    public Muscle getOne() {
        return getMany().getFirst();
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

    @Override
    public long getCreatedAmount() {
        return MuscleName.values().length;
    }

}
