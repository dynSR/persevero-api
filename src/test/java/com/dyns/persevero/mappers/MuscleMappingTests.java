package com.dyns.persevero.mappers;

import com.dyns.persevero.domain.dto.MuscleGroupDto;
import com.dyns.persevero.domain.dto.MuscleDto;
import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.enums.MuscleGroupName;
import com.dyns.persevero.enums.MuscleName;
import com.dyns.persevero.fixtures.impl.MuscleFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MuscleMappingTests {

    private final MuscleFixture fixture = new MuscleFixture();

    @Test
    @DisplayName("Should map from a muscle entity to a muscle DTO")
    public void shouldMapToDto() {
        // GIVEN
        Muscle muscle = fixture.getOne();

        // WHEN
        MuscleDto muscleDto = MuscleMapper.INSTANCE.toDto(muscle);
        log.info("{}", muscleDto);

        // THEN
        assertThat(muscleDto).isNotNull();
        assertThat(muscleDto.getName()).isEqualTo(muscle.getName());
        assertThat(muscleDto.getDescription()).isEqualTo(muscle.getDescription());
        assertThat(muscleDto.getExercises()).isEqualTo(muscle.getExercises());
    }

    @Test
    @DisplayName("Should map from a muscle DTO to a muscle entity")
    public void shouldMapToEntity() {
        // GIVEN
        MuscleDto muscleDto = MuscleDto.builder()
                .id(null)
                .name(MuscleName.FOREARMS)
                .description("Description")
                .exercises(new HashSet<>())
                .build();

        // WHEN
        Muscle muscle = MuscleMapper.INSTANCE.toEntity(muscleDto);
        log.info("{}", muscle);

        // THEN
        assertThat(muscle).isNotNull();
        assertThat(muscle.getName()).isEqualTo(muscle.getName());
        assertThat(muscle.getDescription()).isEqualTo(muscle.getDescription());
        assertThat(muscle.getExercises()).isEqualTo(muscle.getExercises());
    }

    @Test
    @DisplayName("Should map from a muscle DTO to a muscle entity with a muscle group")
    public void shouldMapToEntityWithMuscleGroup() {
        // GIVEN
        MuscleGroupDto muscleGroupDto = MuscleGroupDto.builder()
                .id(null)
                .name(MuscleGroupName.NONE)
                .build();
        MuscleDto muscleDto = MuscleDto.builder()
                .id(null)
                .name(MuscleName.FOREARMS)
                .description("Description")
                .exercises(new HashSet<>())
                .build();
        muscleDto.setMuscleGroup(muscleGroupDto);

        // WHEN
        Muscle muscle = MuscleMapper.INSTANCE.toEntity(muscleDto);
        log.info("{}", muscle);

        // THEN
        assertThat(muscle.getMuscleGroup()).isNotNull();
    }

}
