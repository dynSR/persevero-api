package com.dyns.persevero.repositories;

import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.domain.model.impl.MuscleGroup;
import com.dyns.persevero.enums.MuscleGroupName;
import com.dyns.persevero.fixtures.impl.MuscleFixture;
import com.dyns.persevero.fixtures.impl.MuscleGroupFixture;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

@Slf4j
@Transactional
public class MuscleGroupRepositoryIntegrationTests extends AbstractRepositoryIntegrationTests<
        MuscleGroupRepository,
        MuscleGroup,
        MuscleGroupFixture,
        UUID
        > {

    @Autowired
    protected MuscleRepository muscleRepository;
    private final MuscleFixture muscleFixture = new MuscleFixture();

    @BeforeEach
    @Override
    protected void setDependencies() {
        saveMany();
        muscleFixture.getMany().forEach(muscleRepository::save);
    }

    protected MuscleGroupFixture getFixture() {
        return fixture != null ? fixture : new MuscleGroupFixture();
    }

    @Test
    @DisplayName("Should delete a muscle group without deleting any associated muscle(s)")
    public void givenMuscleGroupWithMuscles_whenDeleted_thenIsDeletedButAssociatedMuscledAreStillPersisted() {
        // GIVEN
        underTest.findByName(MuscleGroupName.CORE).ifPresentOrElse(
                foundMuscleGroup -> {
                    List<Muscle> savedMuscles = List.copyOf(
                            (Collection<? extends Muscle>) muscleRepository.findAll()
                    );
                    savedMuscles.forEach(
                            savedMuscle -> {
                                savedMuscle.setMuscleGroup(foundMuscleGroup);
                                muscleRepository.save(savedMuscle);
                            }
                    );

                    // WHEN
                    underTest.delete(foundMuscleGroup);

                    // THEN
                    assertThat(
                            ((Collection<Muscle>) muscleRepository.findAll()).size()
                    ).isEqualTo(muscleFixture.getCreatedAmount());
                },
                () -> fail(getFailureMessage())
        );
    }

    @Test
    @DisplayName("Should delete a muscle group and set muscle group as null to any associated muscle(s)")
    public void givenMuscleGroupWithMuscles_whenDeleted_thenIsDeletedAndAssociatedMusclesHaveNullMuscleGroup() {
        // GIVEN
        underTest.findByName(MuscleGroupName.CORE).ifPresentOrElse(
                foundMuscleGroup -> {
                    List<Muscle> savedMuscles = List.copyOf(
                            (Collection<? extends Muscle>) muscleRepository.findAll()
                    );
                    savedMuscles.forEach(
                            savedMuscle -> {
                                savedMuscle.setMuscleGroup(foundMuscleGroup);
                                muscleRepository.save(savedMuscle);
                            }
                    );

                    // WHEN
                    underTest.delete(foundMuscleGroup);

                    // THEN
                    assertThat(
                            savedMuscles.stream()
                                    .allMatch(savedMuscle -> savedMuscle.getMuscleGroup() == null)
                    ).isTrue();
                },
                () -> fail(getFailureMessage())
        );
    }

    @ParameterizedTest
    @EnumSource(value = MuscleGroupName.class)
    @DisplayName("Should retrieve a muscle group by name")
    public void givenValidMuscleGroupName_whenQueryingByName_thenReturnsMuscleGroup(MuscleGroupName name) {
        // WHEN
        underTest.findByName(name).ifPresentOrElse(
                foundMuscleGroup -> {
                    // THEN
                    assertThat(foundMuscleGroup.getName()).isEqualTo(name);
                    log.info("Muscle group found : {}", foundMuscleGroup);
                },
                () -> fail(getFailureMessage())
        );
    }
}
