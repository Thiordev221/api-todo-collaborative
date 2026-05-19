package sn.thiordev221.app.service;

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
    
    // CRUD de base
    TodoListResponse createList(TodoListRequest request, Long currentUserId);

    TodoListResponse getListById(Long listId, Long currentUserId);

    Page<TodoListResponse> getMyLists(Long currentUserId, Pageable pageable);

    TodoListResponse updateList(Long listId, TodoListRequest request, Long currentUserId);

    void deleteList(Long listId, Long currentUserId);

    Page<TodoListResponse> getSharedLists(Long currentUserId, Pageable pageable);
}
