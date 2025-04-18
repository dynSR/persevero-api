package com.dyns.persevero.domain.dto;

import com.dyns.persevero.enums.TokenType;
import com.dyns.persevero.validations.validators.EnumValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    @NotBlank
    private String token;

    @NotNull
    @EnumValidator(enumClass = TokenType.class)
    private TokenType type;

    @NotNull
    private boolean isRevoked;

    @NotNull
    private Instant expiresAt;

    @NotNull
    private UserDto owner;

}
