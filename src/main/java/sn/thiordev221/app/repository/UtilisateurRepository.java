package sn.thiordev221.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.thiordev221.app.model.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>{
    boolean existsByEmail(String email);

    Optional<Utilisateur> findByEmail(String email);

    Page<Utilisateur> findAllByActifTrue(Pageable pageable);

    Page<Utilisateur> findAllByPseudoContainingIgnoreCase(String pseudo, Pageable pageable);
}
