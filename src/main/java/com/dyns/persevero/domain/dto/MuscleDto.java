package com.dyns.persevero.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class MuscleDto {
    // TODO: Set validation messages

    @NotNull(message = "")
    @NotBlank(message = "")
    @Size(max = 150, message = "{max}")
    private String name;

    @NotNull(message = "")
    @NotBlank(message = "")
    @Size(max = 200, message = "{max}")
    private String description;

}
