package com.dyns.persevero.fixture;

import com.dyns.persevero.domain.model.MuscleGroup;
import com.dyns.persevero.enums.MuscleGroupType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MuscleGroupFixture {

    private final static int FIXTURES_AMOUNT = MuscleGroupType.values().length;
    private final List<MuscleGroup> muscleGroups = new ArrayList<>();

    public MuscleGroup getOne() {
        int randomIndex = new Random().nextInt(FIXTURES_AMOUNT - 1);
        return getAll().get(randomIndex);
    }

    public List<MuscleGroup> getAll() {
        for (MuscleGroupType type : MuscleGroupType.values()) {
            if (type.name().equals(MuscleGroupType.NONE.name())) continue;

            muscleGroups.add(
                    MuscleGroup.builder()
                            .id(null)
                            .name(type)
                            .build()
            );
        }

        return muscleGroups;
    }

}
