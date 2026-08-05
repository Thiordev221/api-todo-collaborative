package sn.thiordev221.app.service.implementations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.thiordev221.app.custom_exceptons.AccessDeniedException;
import sn.thiordev221.app.custom_exceptons.TodoListNotFoundException;
import sn.thiordev221.app.custom_exceptons.UtilisateurNotFoundException;
import sn.thiordev221.app.dto.requests.TodoListRequest;
import sn.thiordev221.app.dto.responses.TodoListResponse;
import sn.thiordev221.app.mapper.TodoListMapper;
import sn.thiordev221.app.model.TodoList;
import sn.thiordev221.app.model.Utilisateur;
import sn.thiordev221.app.repository.TodoListRepository;
import sn.thiordev221.app.repository.UtilisateurRepository;
import sn.thiordev221.app.service.contrats.TodoListService;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TodoListServiceImpl implements TodoListService{

    private final TodoListRepository todoListRepository;
    private final TodoListMapper todoListMapper;
    private final UtilisateurRepository utilisateurRepository;
    private final PermissionHelper helper;

    
    @Override
    public TodoListResponse createList(TodoListRequest request, Long currentUserId) {
        log.info("Création d'une liste pour l'utilisateur : {}", currentUserId);
        Utilisateur owner = utilisateurRepository.findById(currentUserId)
            .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur", "id", currentUserId));

        TodoList newList = todoListMapper.toTodoList(request);
        newList.setProprietaire(owner);
        
        TodoList saved = todoListRepository.save(newList);
        return mapToResponse(saved, "OWNER");
    }

    @Override
    @Transactional(readOnly = true)
    public TodoListResponse getListById(Long listId, Long currentUserId) {
        TodoList list = todoListRepository.findById(listId)
            .orElseThrow(() -> new TodoListNotFoundException("TodoList", "id", listId));

        String permission = helper.calculatePermissions(list, currentUserId);
        if ("NONE".equals(permission)) {
            throw new AccessDeniedException("Utilisateur", "id", currentUserId);
        }

        return mapToResponse(list, permission);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TodoListResponse> getMyLists(Long currentUserId, Pageable pageable) {
        return todoListRepository.findAllByProprietaireId(currentUserId, pageable)
            .map(list -> mapToResponse(list, "OWNER"));
    }

    @Override
    public TodoListResponse updateList(Long listId, TodoListRequest request, Long currentUserId) {
        TodoList list = todoListRepository.findById(listId)
            .orElseThrow(() -> new TodoListNotFoundException("TodoList", "id", listId));

        // Seul le propriétaire peut modifier le titre/description de la liste
        if (!list.getProprietaire().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Utilisateur", "id", currentUserId);
        }

        list.setTitre(request.titre());
        list.setDescription(request.description());
        return mapToResponse(todoListRepository.save(list), "OWNER");
    }

    @Override
    public void deleteList(Long listId, Long currentUserId) {
        TodoList list = todoListRepository.findById(listId)
            .orElseThrow(() -> new TodoListNotFoundException("TodoList", "id", listId));

        if (!list.getProprietaire().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Utilisateur", "id", currentUserId);
        }

        todoListRepository.delete(list);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TodoListResponse> getAllListsSharedToCurrentUser(Long currentUserId, Pageable pageable) {
        log.info("Récupération des listes de tâches partagées avec l'utilisateur avec l'id : {} avec pagination : page {}, size {}", currentUserId, pageable.getPageNumber(), pageable.getPageSize());
        Page<TodoList> sharedListsPage = todoListRepository.findAllSharedToCurrentUser(currentUserId, pageable);
        return sharedListsPage.map(list -> {
            String permission = helper.calculatePermissions(list, currentUserId);
            return mapToResponse(list, permission);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TodoListResponse> getAllListsCurrentUserShared(Long currentUserId, Pageable pageable) {
        log.info("Récupération des listes de tâches partagées par l'utilisateur avec l'id : {} avec pagination : page {}, size {}", currentUserId, pageable.getPageNumber(), pageable.getPageSize());
        Page<TodoList> sharedListsPage = todoListRepository.findAllSharedByCurrentUser(currentUserId, pageable);
        return sharedListsPage.map(list -> mapToResponse(list, "OWNER"));
    }



    private TodoListResponse mapToResponse(TodoList list, String permission) {
        TodoListResponse response = todoListMapper.toTodoListResponse(list);
        // On reconstruit le record avec la permission (car les records sont immuables)
        return new TodoListResponse(
            response.id(),
            response.titre(),
            response.description(),
            response.dateCreation(),
            response.proprietaireId(),
            response.proprietairePseudo(),
            response.nbTaches(),
            permission
        );
    }
}
