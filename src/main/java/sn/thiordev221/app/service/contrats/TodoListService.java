package sn.thiordev221.app.service.contrats;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sn.thiordev221.app.dto.requests.TodoListRequest;
import sn.thiordev221.app.dto.responses.TodoListResponse;

/**
 * Service pour gérer les listes de tâches (TodoLists) de l'application.
 * Ce service peut inclure des méthodes pour créer, lire, mettre à jour et supprimer
 * Mais auusi récupèrer les listes partagés entre utilisateur
 */
public interface TodoListService {
    
    /**
     * Crée une nouvelle liste de tâches pour un utilisateur donné.
     * @param request Les informations de la liste à créer.
     * @param currentUserId L'identifiant de l'utilisateur qui crée la liste.
     * @return TodoListResponse La liste de tâches créée.
     */
    TodoListResponse createList(TodoListRequest request, Long currentUserId);

    /**
     * Récupère une liste de tâches par son identifiant, en vérifiant que l'utilisateur a le droit d'y accéder.
     * @param listId L'identifiant de la liste à récupérer.
     * @param currentUserId L'identifiant de l'utilisateur qui fait la demande.
     * @return TodoListResponse La liste de tâches correspondante.
     */
    TodoListResponse getListById(Long listId, Long currentUserId);

    /**
     * Récupère toutes les listes de tâches appartenant à un utilisateur donné, avec pagination.
     * @param currentUserId L'identifiant de l'utilisateur.
     * @param pageable Les informations de pagination.
     * @return Page<TodoListResponse> La page de listes de tâches correspondante.
     */
    Page<TodoListResponse> getMyLists(Long currentUserId, Pageable pageable);

    /**
     * Met à jour une liste de tâches existante, en vérifiant que l'utilisateur a le droit de la modifier.
     * @param listId L'identifiant de la liste à mettre à jour.
     * @param request Les nouvelles informations de la liste.
     * @param currentUserId L'identifiant de l'utilisateur qui fait la demande.
     * @return TodoListResponse La liste de tâches mise à jour.
     */
    TodoListResponse updateList(Long listId, TodoListRequest request, Long currentUserId);

    /**
     * Supprime une liste de tâches, en vérifiant que l'utilisateur a le droit de la supprimer.
     * @param listId L'identifiant de la liste à supprimer.
     * @param currentUserId L'identifiant de l'utilisateur qui fait la demande.
     */
    void deleteList(Long listId, Long currentUserId);

    /**
     * Récupère toutes les listes de tâches partagées avec un utilisateur donné, avec pagination.
     * @param currentUserId L'identifiant de l'utilisateur.
     * @param pageable Les informations de pagination.
     * @return Page<TodoListResponse> La page de listes de tâches partagées correspondante.
     */
    Page<TodoListResponse> getAllListsCurrentUserShared(Long currentUserId, Pageable pageable);

    Page<TodoListResponse> getAllListsSharedToCurrentUser(Long currentUserId, Pageable pageable);
}
