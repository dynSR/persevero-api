package com.dyns.persevero.domain.dto;

import com.dyns.persevero.enums.TokenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Data
@Getter
@Setter
@Builder
public class TokenDto {
    // TODO: Set validation messages

    @NotNull(message = "")
    @NotBlank(message = "")
    private String token;

    @NotNull(message = "")
    private TokenType type;

    @NotNull(message = "")
    private boolean isRevoked;

    @NotNull(message = "")
    private Instant expiresAt;
}
