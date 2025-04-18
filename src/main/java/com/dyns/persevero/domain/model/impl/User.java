package com.dyns.persevero.domain.model.impl;

import com.dyns.persevero.utils.Validate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private UUID id;

    @Column(
            length = 254,
            nullable = false
    )
    @Email(regexp = Validate.EMAIL_REGEX)
    @Size(max = 254)
    private String email;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @CreationTimestamp
    @Column(
            name = "created_at",
            columnDefinition = "TIMESTAMP",
            updatable = false
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(
            name = "updated_at",
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime updatedAt;

    @Column(
            name = "active",
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private boolean isActive;

    @Column(
            name = "enabled",
            columnDefinition = "BOOLEAN DEFAULT FALSE"
    )
    private boolean isEnabled;

    private void setId(UUID uuid) {
        id = uuid;
    }

    private void setCreatedAt(LocalDateTime dateTime) {
        createdAt = dateTime;
    }

}
