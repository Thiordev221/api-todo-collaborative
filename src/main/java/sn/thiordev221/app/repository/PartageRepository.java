package sn.thiordev221.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.thiordev221.app.model.Partage;

@Repository
public interface PartageRepository extends JpaRepository<Partage, Long>{
    
    //Récuperer tous les partages d'un utilisateur
    List<Partage> findAllByInviteId(Long inviteId);
    
    //Récupérer tous les partages d'un propriétaire
    Page<Partage> findAllByTodoListIdAndTodoListProprietaireId(Long todoListId, Long proprietaireId, Pageable pageable);

    //Récuperer un partage par l'ID de la liste et l'ID de l'utilisateur
    Optional<Partage> findByTodoListIdAndInviteId(Long listId, Long inviteId);

    //Vérifier si un partage existe pour une liste et un Invite donné
    boolean existsByTodoListIdAndInviteId(Long listId, Long inviteId);
}
