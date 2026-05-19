package sn.thiordev221.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sn.thiordev221.app.dto.requests.TacheCreateRequest;
import sn.thiordev221.app.dto.requests.TacheUpdateRequest;
import sn.thiordev221.app.dto.responses.TacheResponse;

public interface TacheService {
    //CRUD de base pour les tâches
    //Ajouter une tâche à une liste de tâches spécifique
    TacheResponse ajouterTache(Long listId, TacheCreateRequest request, Long currentUserId);

    //Mettre à jour une tâche existante du propriétaire
    TacheResponse modifierTache(Long tacheId, TacheUpdateRequest request, Long currentUserId);

    //Supprimer une tâche existante du propriétaire
    void supprimerTache(Long tacheId, Long currentUserId);

    //Récupèrer toutes les tâches de la liste du propriétaire
    Page<TacheResponse> getTachesDeLaListe(Long listId, Long currentUserId, Pageable pageable);

    //Changer le statut d'une tâche (terminée ou non terminée)
    TacheResponse toggleStatus(Long tacheId, Long currentUserId);
}
