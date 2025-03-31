package com.dyns.persevero.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Getter
@Setter
public class ErrorResponse {

    private HttpStatus status;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    @Builder
    public ErrorResponse(
            @NotNull final HttpStatus status,
            @NotNull final String message
    ) {
        this.status = Objects.requireNonNull(status);
        this.message = Objects.requireNonNull(message);
        this.timestamp = LocalDateTime.now();
    }

}
