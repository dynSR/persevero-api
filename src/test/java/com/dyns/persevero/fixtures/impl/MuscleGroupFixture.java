package com.dyns.persevero.fixtures.impl;

import com.dyns.persevero.domain.model.impl.MuscleGroup;
import com.dyns.persevero.enums.MuscleGroupName;
import com.dyns.persevero.fixtures.Fixture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MuscleGroupFixture implements Fixture<MuscleGroup> {

    private final static int FIXTURES_AMOUNT = MuscleGroupName.values().length;
    private final List<MuscleGroup> muscleGroups = new ArrayList<>();

    public MuscleGroup getOne() {
        int randomIndex = new Random().nextInt(FIXTURES_AMOUNT - 1);
        return getMany().get(randomIndex);
    }

    public List<MuscleGroup> getMany() {
        for (MuscleGroupName name : MuscleGroupName.values()) {
            muscleGroups.add(
                    MuscleGroup.builder()
                            .id(null)
                            .name(name)
                            .muscles(new HashSet<>())
                            .build()
            );
        }

        return muscleGroups;
    }

}
