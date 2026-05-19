package sn.thiordev221.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sn.thiordev221.app.model.TodoList;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    // Récupérer une liste de tâches par son ID et l'ID de son propriétaire
    Optional<TodoList> findByIdAndProprietaireId(Long id, Long proprietaireId);

    //Récupérer toutes les listes d'un propriétaire par son id
    Page<TodoList> findAllByProprietaireId(Long proprietaireId, Pageable pageable);

    // Récupérer toutes les listes de tâches d'un propriétaire avec leurs tâches associées
    @Query("SELECT t FROM TodoList t JOIN t.taches WHERE t.proprietaire.id = :proprietaireId")
    Page<TodoList> findAllWithTachesByProprietaireId(@Param("proprietaireId") Long proprietaireId, Pageable pageable);

    // Récupérer une liste de tâches par son ID et l'ID d'un utilisateur invité
    @Query("SELECT t FROM TodoList t JOIN t.partages p WHERE t.id = :id AND p.invite.id = :inviteId")
    Optional<TodoList> findByIdAndInviteId(@Param("id") Long id, @Param("inviteId") Long inviteId);

    // Compter le nombre de listes de tâches appartenant à un utilisateur
    long countByProprietaireId(Long proprietaireId);
}
