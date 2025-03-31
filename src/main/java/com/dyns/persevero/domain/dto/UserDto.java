package com.dyns.persevero.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class UserDto {
    // TODO: Set validation messages

    @NotNull(message = "")
    @NotBlank(message = "")
    private String email;

    private boolean isActive;

    private boolean isEnabled;
}
