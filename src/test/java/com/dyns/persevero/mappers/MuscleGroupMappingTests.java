package com.dyns.persevero.mappers;

import com.dyns.persevero.domain.dto.MuscleGroupDto;
import com.dyns.persevero.domain.model.impl.MuscleGroup;
import com.dyns.persevero.enums.MuscleGroupName;
import com.dyns.persevero.fixtures.impl.MuscleGroupFixture;
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
public class MuscleGroupMappingTests {

    private final MuscleGroupFixture fixture = new MuscleGroupFixture();

    @Test
    @DisplayName("Should map from a muscle group entity to a muscle group DTO")
    public void shouldMapToDto() {
        // GIVEN
        MuscleGroup muscleGroup = fixture.getOne();

        // WHEN
        MuscleGroupDto muscleGroupDto = MuscleGroupMapper.INSTANCE.toDto(muscleGroup);
        log.info("{}", muscleGroupDto);

        // THEN
        assertThat(muscleGroupDto).isNotNull();
        assertThat(muscleGroupDto.getName()).isEqualTo(muscleGroup.getName());
        assertThat(muscleGroupDto.getMuscles()).isEqualTo(muscleGroup.getMuscles());
    }

    @Test
    @DisplayName("Should map from a muscle group DTO to a muscle group entity")
    public void shouldMapToEntity() {
        // GIVEN
        MuscleGroupDto muscleGroupDto = MuscleGroupDto.builder()
                .id(null)
                .name(MuscleGroupName.CORE)
                .muscles(new HashSet<>())
                .build();

        // WHEN
        MuscleGroup muscleGroup = MuscleGroupMapper.INSTANCE.toEntity(muscleGroupDto);
        log.info("{}", muscleGroup);

        // THEN
        assertThat(muscleGroup).isNotNull();
        assertThat(muscleGroup.getName()).isEqualTo(muscleGroup.getName());
        assertThat(muscleGroup.getMuscles()).isEqualTo(muscleGroup.getMuscles());
    }

}
