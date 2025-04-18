package com.dyns.persevero.domain.dto;

import com.dyns.persevero.validations.validators.EmailValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private UUID id;

    @EmailValidator
    private String email;

    private boolean isActive;

    private boolean isEnabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private void setId(UUID uuid) {
        id = uuid;
    }

    private void setCreatedAt(LocalDateTime dateTime) {
        createdAt = dateTime;
    }

}
