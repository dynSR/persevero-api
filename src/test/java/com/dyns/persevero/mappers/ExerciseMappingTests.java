package com.dyns.persevero.mappers;

import com.dyns.persevero.domain.dto.ExerciseDto;
import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.fixtures.impl.ExerciseFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ExerciseMappingTests {

    private final ExerciseFixture fixture = new ExerciseFixture();

    @Test
    @DisplayName("Should map from an exercise entity to an exercise DTO")
    public void shouldMapToDto() {
        // GIVEN
        Exercise exercise = fixture.getOne();

        // WHEN
        ExerciseDto exerciseDto = ExerciseMapper.INSTANCE.toDto(exercise);
        log.info("{}", exerciseDto);

        // THEN
        assertThat(exerciseDto).isNotNull();
        assertThat(exerciseDto.getName()).isEqualTo(exercise.getName());
        assertThat(exerciseDto.getDescription()).isEqualTo(exercise.getDescription());
        assertThat(exerciseDto.getSets()).isEqualTo(exercise.getSets());
        assertThat(exerciseDto.getReps()).isEqualTo(exercise.getReps());
        assertThat(exerciseDto.getWeight()).isEqualTo(exercise.getWeight());
        assertThat(exerciseDto.getMuscles()).isEqualTo(exercise.getMuscles());
    }

    @Test
    @DisplayName("Should map from an exercise DTO to an exercise entity")
    public void shouldMapToEntity() {
        // GIVEN
        ExerciseDto exerciseDto = ExerciseDto.builder()
                .id(null)
                .name("Exercise Dto")
                .description("Description")
                .sets(4)
                .reps(12)
                .weight(BigDecimal.valueOf(12.52))
                .muscles(new HashSet<>())
                .build();

        // WHEN
        Exercise exercise = ExerciseMapper.INSTANCE.toEntity(exerciseDto);
        log.info("{}", exercise);

        // THEN
        assertThat(exercise).isNotNull();
        assertThat(exercise.getName()).isEqualTo(exercise.getName());
        assertThat(exercise.getDescription()).isEqualTo(exercise.getDescription());
        assertThat(exercise.getSets()).isEqualTo(exercise.getSets());
        assertThat(exercise.getReps()).isEqualTo(exercise.getReps());
        assertThat(exercise.getWeight()).isEqualTo(exercise.getWeight());
        assertThat(exercise.getMuscles()).isEqualTo(exercise.getMuscles());
    }

}
