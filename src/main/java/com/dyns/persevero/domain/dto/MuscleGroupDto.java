package com.dyns.persevero.domain.dto;

import com.dyns.persevero.enums.MuscleGroupName;
import com.dyns.persevero.validations.validators.EnumValidator;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuscleGroupDto {

    private UUID id;

    @EnumValidator(enumClass = MuscleGroupName.class)
    private MuscleGroupName name;

    private Set<MuscleDto> muscles;

    private void setId(UUID uuid) {
        id = uuid;
    }

}
