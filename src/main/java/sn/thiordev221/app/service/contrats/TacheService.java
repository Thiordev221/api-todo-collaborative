package sn.thiordev221.app.service.contrats;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sn.thiordev221.app.dto.requests.TacheCreateRequest;
import sn.thiordev221.app.dto.requests.TacheUpdateRequest;
import sn.thiordev221.app.dto.responses.TacheResponse;

public interface TacheService {
    /**
     * Ajoute une nouvelle tâche à une liste de tâches, en vérifiant que l'utilisateur a le droit d'ajouter des tâches à cette liste.
     * @param listId L'identifiant de la liste à laquelle ajouter la tâche.
     * @param request Les informations de la tâche à créer.
     * @param currentUserId L'identifiant de l'utilisateur qui fait la demande.
     * @return TacheResponse La tâche créée.
     */
    TacheResponse ajouterTache(Long listId, TacheCreateRequest request, Long currentUserId);

    /**
     * Modifie une tâche existante, en vérifiant que l'utilisateur a le droit de modifier cette tâche.
     * @param tacheId L'identifiant de la tâche à modifier.
     * @param request Les nouvelles informations de la tâche.
     * @param currentUserId L'identifiant de l'utilisateur qui fait la demande.
     * @return TacheResponse La tâche mise à jour.
     */
    TacheResponse modifierTache(Long tacheId, TacheUpdateRequest request, Long currentUserId);

    /**
     * Supprime une tâche, en vérifiant que l'utilisateur a le droit de supprimer cette tâche.
     * @param tacheId L'identifiant de la tâche à supprimer.
     * @param currentUserId L'identifiant de l'utilisateur qui fait la demande.
     */
    void supprimerTache(Long tacheId, Long currentUserId);

    /**
     * Récupère toutes les tâches d'une liste de tâches, en vérifiant que l'utilisateur a le droit d'accéder à cette liste.
     * @param listId L'identifiant de la liste dont on veut récupérer les tâches.
     * @param currentUserId L'identifiant de l'utilisateur qui fait la demande.
     * @param pageable Les informations de pagination.
     * @return Page<TacheResponse> La page de tâches correspondante.
     */
    Page<TacheResponse> getTachesDeLaListe(Long listId, Long currentUserId, Pageable pageable);

    /**
     * Change le statut d'une tâche (terminée ou non terminée), en vérifiant que l'utilisateur a le droit de modifier cette tâche.
     * @param tacheId L'identifiant de la tâche dont on veut changer le statut.
     * @param currentUserId L'identifiant de l'utilisateur qui fait la demande.
     * @return TacheResponse La tâche dont le statut a été changé.
     */
    TacheResponse toggleStatus(Long tacheId, Long currentUserId);
}
