package sn.thiordev221.app.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="refresh_tokens")
public class RefreshToken {

    @Id
    private String token; // UUID v4 — opaque, pas un JWT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Utilisateur utilisateur;    

    @Column(nullable = false)
    private Instant expiresAt;

    // null = token valide. Rempli quand révoqué (logout, rotation)
    private Instant revokedAt;

    // Pour détecter les connexions suspectes depuis des appareils inconnus
    private String deviceInfo;
    private String ipAddress;

    public boolean isValid() {
        return revokedAt == null && Instant.now().isBefore(expiresAt);
    }
    
}
