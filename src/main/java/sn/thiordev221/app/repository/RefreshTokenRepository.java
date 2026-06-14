package sn.thiordev221.app.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sn.thiordev221.app.model.RefreshToken;
import sn.thiordev221.app.model.Utilisateur;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String>{
    
    Optional<RefreshToken> findById(String id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE RefreshToken r SET r.revokedAt = :now WHERE r.utilisateur = :user")
    void revokeAllByUser(Utilisateur user, Instant now);
}
