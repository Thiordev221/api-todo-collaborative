package sn.thiordev221.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.thiordev221.app.model.Tache;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Long>{
    // Récupérer une tâche par son ID et l'ID de sa liste
    Optional<Tache> findByIdAndTodoListId(Long id, Long todoListId);

    // Récupérer toutes les tâches d'une liste
    Page<Tache> findAllByTodoListId(Long todoListId, Pageable pageable);

    //Récupérer les tâches d'une liste triées par date de creation décroissante
    Page<Tache> findByTodoListIdOrderByDateCreationDesc(Long listId, Pageable pageable);

    // Compter le nombre de tâches non terminées dans une liste
    long countByTodoListIdAndTermineFalse(Long listId);
}
