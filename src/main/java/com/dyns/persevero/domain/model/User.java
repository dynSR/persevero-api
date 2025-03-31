package com.dyns.persevero.domain.model;

import com.dyns.persevero.utils.EmailValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(
                name = "UX_users_email_name",
                columnNames = {
                        "email",
                        "name"
                }
        )
)

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(
            length = 254,
            nullable = false
    )
    @Email(regexp = EmailValidator.EMAIL_REGEX)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @Column(
            name = "created_at",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            updatable = false
    )
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(
            name = "updated_at",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(
            name = "active",
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private boolean isActive = true;

    @Column(
            name = "enabled",
            columnDefinition = "BOOLEAN DEFAULT FALSE"
    )
    private boolean isEnabled = false;

}
