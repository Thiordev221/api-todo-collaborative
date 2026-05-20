package sn.thiordev221.app.service.implementations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.thiordev221.app.custom_exceptons.AccessDeniedException;
import sn.thiordev221.app.custom_exceptons.InvalidPermissionException;
import sn.thiordev221.app.custom_exceptons.TacheNotFoundException;
import sn.thiordev221.app.custom_exceptons.TodoListNotFoundException;
import sn.thiordev221.app.dto.requests.TacheCreateRequest;
import sn.thiordev221.app.dto.requests.TacheUpdateRequest;
import sn.thiordev221.app.dto.responses.TacheResponse;
import sn.thiordev221.app.mapper.TacheMapper;
import sn.thiordev221.app.model.Tache;
import sn.thiordev221.app.model.TodoList;
import sn.thiordev221.app.repository.TacheRepository;
import sn.thiordev221.app.repository.TodoListRepository;
import sn.thiordev221.app.service.contrats.TacheService;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TacheServiceImpl implements TacheService{

    private final TacheRepository tacheRepository;
    private final TacheMapper tacheMapper;
    private final TodoListRepository todoListRepository;
    private final PermissionHelper helper;

     /**
     * Calcule les permissions de l'utilisateur actuel sur une liste donnée.
     * @param list La liste de tâches.
     * @param currentUserId L'identifiant de l'utilisateur actuel.
     * @return String "OWNER", "EDITOR", "VIEWER" ou "NONE" selon les permissions de l'utilisateur.
     */
    
    @Override
    @Transactional
    public TacheResponse ajouterTache(Long listId, TacheCreateRequest request, Long currentUserId) {
        log.info("Ajout d'une tâche dans la liste {}", listId);
        TodoList list = todoListRepository.findById(listId)
            .orElseThrow(() -> new TodoListNotFoundException("TodoList", "id", listId));

        String permission = helper.calculatePermissions(list, currentUserId);

        log.info("Permission de l'utilisateur {} sur la liste {} : {}", currentUserId, listId, permission);
        if ("ONLY_READ".equals(permission) || "NONE".equals(permission)) {
            throw new InvalidPermissionException(listId, permission);
        }

        Tache tacheMapped = tacheMapper.toTache(request);
        tacheMapped.setTodoList(list);
        Tache saved = tacheRepository.save(tacheMapped);
        return tacheMapper.toTacheResponse(saved);
    }

    @Override
    @Transactional
    public TacheResponse modifierTache(Long tacheId, TacheUpdateRequest request, Long currentUserId) {
        log.info("Modification d'une tâche dans la liste {}", tacheId);
        log.info("Vérification des permissions de l'utilisateur {} pour la tâche {}", currentUserId, tacheId);
        Tache tache = tacheRepository.findById(tacheId)
            .orElseThrow(() -> new TacheNotFoundException("tache", "id", tacheId));

        log.info("Vérification des droits de l'utilisateur avec id : {} ", currentUserId);
        TodoList list = tache.getTodoList();
        String permission = helper.calculatePermissions(list, currentUserId);

        if ("ONLY_READ".equals(permission) || "NONE".equals(permission)) {
            throw new InvalidPermissionException(list.getId(), permission);
        }

        tache.setTitre(request.titre());
        tache.setDescription(request.description());
        Tache updated = tacheRepository.save(tache);
        log.info("Tache avec id : {} modifié et enregistré avec succès", tache.getId());
        return tacheMapper.toTacheResponse(updated);
    }

    @Override
    @Transactional
    public void supprimerTache(Long tacheId, Long currentUserId) {
        log.info("Suppression d'une tâche dans la liste {}", tacheId);
        log.info("Vérification des permissions de l'utilisateur {} pour la tâche {}", currentUserId, tacheId);
        Tache tache = tacheRepository.findById(tacheId)
            .orElseThrow(() -> new TacheNotFoundException("tache", "id", tacheId));

        log.info("Vérification des droits de l'utilisateur avec id : {} ", currentUserId);
        TodoList list = tache.getTodoList();
        String permission = helper.calculatePermissions(list, currentUserId);

        if ("ONLY_READ".equals(permission) || "NONE".equals(permission)) {
            throw new InvalidPermissionException(list.getId(), permission);
        }
        tacheRepository.delete(tache);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TacheResponse> getTachesDeLaListe(Long listId, Long currentUserId, Pageable pageable) {
        log.info("Récupération des tâches de la liste {} pour l'utilisateur {} avec pagination : page {}, size {}", listId, currentUserId, pageable.getPageNumber(), pageable.getPageSize());
        TodoList list = todoListRepository.findById(listId)
            .orElseThrow(() -> new TodoListNotFoundException("TodoList", "id", listId));

        String permission = helper.calculatePermissions(list, currentUserId);
        if ("NONE".equals(permission)) {
            throw new AccessDeniedException("Utilisateur", "id", currentUserId);
        }

        return tacheRepository.findAllByTodoListId(listId, pageable)
            .map(tacheMapper::toTacheResponse);
    }

    @Override
    @Transactional
    public TacheResponse toggleStatus(Long tacheId, Long currentUserId) {
        log.info("Changement de statut d'une tâche dans la liste {}", tacheId);
        log.info("Vérification des permissions de l'utilisateur {} pour la tâche {}", currentUserId, tacheId);
        Tache tache = tacheRepository.findById(tacheId)
            .orElseThrow(() -> new TacheNotFoundException("tache", "id", tacheId));

        log.info("Vérification des droits de l'utilisateur avec id : {} ", currentUserId);
        TodoList list = tache.getTodoList();
        String permission = helper.calculatePermissions(list, currentUserId);

        if ("ONLY_READ".equals(permission) || "NONE".equals(permission)) {
            throw new InvalidPermissionException(list.getId(), permission);
        }

        tache.setTermine(!tache.isTermine());
        Tache updated = tacheRepository.save(tache);
        log.info("Tache avec id : {} statut changé et enregistré avec succès", tache.getId());
        return tacheMapper.toTacheResponse(updated);
    }


}
