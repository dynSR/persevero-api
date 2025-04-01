package com.dyns.persevero.fixture;

import com.dyns.persevero.domain.model.Muscle;
import com.dyns.persevero.enums.MuscleType;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MuscleFixture {

    private final static int FIXTURES_AMOUNT = MuscleType.values().length;
    private final List<Muscle> muscles = new ArrayList<>();

    public Muscle getOne() {
        int randomIndex = new Random().nextInt(FIXTURES_AMOUNT - 1);
        return getAll(true).get(randomIndex);
    }

    public List<Muscle> getAll(boolean isDescriptionEmpty) {
        for (MuscleType type : MuscleType.values()) {
            if (type.name().equals(MuscleType.NONE.name())) continue;

            muscles.add(Muscle.builder()
                    .id(null)
                    .name(type)
                    .description(isDescriptionEmpty
                            ? Strings.EMPTY
                            : "Muscle description"
                    )
                    .build()
            );
        }

        return muscles;
    }
}
