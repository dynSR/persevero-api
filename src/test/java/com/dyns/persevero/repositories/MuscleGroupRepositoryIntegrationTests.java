//package com.dyns.persevero.repositories;
//
//import com.dyns.persevero.domain.model.impl.Muscle;
//import com.dyns.persevero.domain.model.impl.MuscleGroup;
//import com.dyns.persevero.enums.MuscleGroupName;
//import com.dyns.persevero.fixtures.impl.MuscleFixture;
//import com.dyns.persevero.fixtures.impl.MuscleGroupFixture;
//import com.dyns.persevero.repositories.impl.AbstractRepositoryIntegrationTests;
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.EnumSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.fail;
//
//@Slf4j
//@Transactional
//public class MuscleGroupRepositoryIntegrationTests extends AbstractRepositoryIntegrationTests<
//        MuscleGroupRepository,
//        MuscleGroup,
//        MuscleGroupFixture,
//        UUID,
//        MuscleGroupName
//        > {
//
//    private MuscleGroup savedDefaultMuscleGroup;
//
//    @Autowired
//    protected MuscleRepository muscleRepository;
//    private final MuscleFixture muscleFixture = new MuscleFixture();
//    private final Set<Muscle> muscleSet = new HashSet<>();
//
//    @BeforeEach
//    @Override
//    public void init() {
//        super.init();
//
//        underTest.findByName(MuscleGroupName.NONE).ifPresentOrElse(
//                mg -> savedDefaultMuscleGroup = mg,
//                () -> fail("Expected default muscle group")
//        );
//
//        // Save all muscles into test db.
//        muscleFixture.getMany().forEach(
//                m -> {
//                    m.setMuscleGroup(savedDefaultMuscleGroup);
//                    Muscle savedMuscle = muscleRepository.save(m);
//                    muscleSet.add(savedMuscle);
//                }
//        );
//    }
//
//    @Override
//    protected MuscleGroupFixture getFixture() {
//        return fixture != null ? fixture : new MuscleGroupFixture();
//    }
//
//    /**
//     * Delete test with cascade check :
//     * <p> - Given one persisted muscle group
//     * <p> - When deleting a muscle group
//     * <p> - Then asserts that this muscle group cannot be retrieved anymore
//     * <p> - Then asserts that associated muscles are removed in cascade
//     */
//    @Test
//    @DisplayName("Should delete a muscle group and all associated muscles")
//    public void shouldDeleteMuscleGroupAndAllAssociatedMuscles() {
//        MuscleGroup muscleGroup = getFixture().getOne();
//        Set<Muscle> muscleSubSet = muscleSet.stream()
//                .limit(muscleSet.size() / 2)
//                .collect(Collectors.toSet());
//        muscleGroup.setMuscles(muscleSubSet);
//
//        MuscleGroup savedMuscleGroup = underTest.save(muscleGroup);
//        assertThat(savedMuscleGroup.getMuscles().size()).isNotZero();
//        assertThat(savedMuscleGroup.getMuscles()).isEqualTo(muscleGroup.getMuscles());
//
//        underTest.findById(savedMuscleGroup.getId()).ifPresentOrElse(
//                mg -> {
//                    assertThat(mg.getMuscles().containsAll(muscleSubSet)).isTrue();
//                    underTest.delete(mg);
//                }, () -> fail("Was expecting a muscle group")
//        );
//
//        Set<Muscle> musclesFromDb = Set.copyOf(
//                (Collection<? extends Muscle>) muscleRepository.findAll()
//        );
//        assertThat(musclesFromDb.size()).isEqualTo(muscleSubSet.size());
//    }
//
//    /**
//     * Find by name test :
//     * <p> - Given persisted muscle groups
//     * <p> - When finding a muscle group by name
//     * <p> - Then asserts that this muscle group exists and is persisted
//     */
//    @ParameterizedTest
//    @EnumSource(value = MuscleGroupName.class)
//    @DisplayName("Should retrieve a muscle group by name")
//    public void shouldRetrieveAMuscleGroupByName(MuscleGroupName name) {
//        getFixture().getMany().forEach(underTest::save);
//
//        underTest.findByName(name).ifPresentOrElse(
//                mg -> assertThat(mg.getName()).isEqualTo(name),
//                () -> fail("Was expecting to find a muscle a group")
//        );
//    }
//}
