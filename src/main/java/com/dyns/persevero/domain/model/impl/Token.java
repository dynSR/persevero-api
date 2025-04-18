package com.dyns.persevero.domain.model.impl;

import com.dyns.persevero.domain.model.Model;
import com.dyns.persevero.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tokens")
public class Token implements Model<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType type;

    @Column(
            name = "revoked",
            columnDefinition = "BOOLEAN",
            nullable = false
    )
    private boolean isRevoked;

    @Column(
            name = "expires_at",
            columnDefinition = "TIMESTAMP",
            updatable = false,
            nullable = false
    )
    private Instant expiresAt;

    private void setId(UUID uuid) {
        id = uuid;
    }

}
