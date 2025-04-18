package com.dyns.persevero.fixtures.impl;

import com.dyns.persevero.domain.model.impl.MuscleGroup;
import com.dyns.persevero.enums.MuscleGroupName;
import com.dyns.persevero.fixtures.Fixture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MuscleGroupFixture implements Fixture<MuscleGroup> {

    private final List<MuscleGroup> muscleGroups = new ArrayList<>();

    public MuscleGroup getOne() {
        return getMany().getFirst();
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

    @Override
    public long getCreatedAmount() {
        return MuscleGroupName.values().length;
    }

}
