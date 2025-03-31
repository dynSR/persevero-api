package com.dyns.persevero.domain.model;

import com.dyns.persevero.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import org.apache.logging.log4j.util.Strings;

import java.io.Serial;
import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tokens")
public class Token implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(nullable = false, updatable = false)
    private String token = Strings.EMPTY;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType type = TokenType.NONE;

    @Column(
            name = "revoked",
            columnDefinition = "BOOLEAN DEFAULT FALSE",
            nullable = false
    )
    private boolean isRevoked = false;

    @Column(
            name = "expires_at",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            updatable = false,
            nullable = false
    )
    private Instant expiresAt = Instant.now(Clock.systemDefaultZone());

}
