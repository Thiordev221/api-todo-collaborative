package sn.thiordev221.app.service.implementations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.thiordev221.app.custom_exceptons.AccessDeniedException;
import sn.thiordev221.app.custom_exceptons.AlreadySharedException;
import sn.thiordev221.app.custom_exceptons.PartageNotFoundException;
import sn.thiordev221.app.custom_exceptons.SelfSharingException;
import sn.thiordev221.app.custom_exceptons.TodoListNotFoundException;
import sn.thiordev221.app.custom_exceptons.UtilisateurNotFoundException;
import sn.thiordev221.app.dto.requests.PartageRequest;
import sn.thiordev221.app.dto.responses.PartageResponse;
import sn.thiordev221.app.mapper.PartageMapper;
import sn.thiordev221.app.model.Partage;
import sn.thiordev221.app.model.TodoList;
import sn.thiordev221.app.model.Utilisateur;
import sn.thiordev221.app.repository.PartageRepository;
import sn.thiordev221.app.repository.TodoListRepository;
import sn.thiordev221.app.repository.UtilisateurRepository;
import sn.thiordev221.app.service.contrats.PartageService;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PartageServiceImpl implements PartageService{

    private final PartageRepository partageRepository;
    private final PartageMapper partageMapper;
    private final UtilisateurRepository utilisateurRepository;
    private final TodoListRepository todoListRepository;

    
    @Override
    @Transactional
    public PartageResponse inviterUtilisateur(Long listId, PartageRequest request, Long currentUserId) {
        log.info("Invitation de  l'utilisateur avec id : {} à l'invité avec email {} sur la liste de taches avec id : {}", listId, request.inviteEmail(), currentUserId);
        Utilisateur invite = utilisateurRepository.findByEmail(request.inviteEmail())
            .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur", "email", request.inviteEmail()));
        
        log.info("Utilisateur invité trouvé : {}", invite.getPseudo());
        log.info("Vérification des permissions de l'utilisateur {} pour la liste {}", currentUserId, listId);

        TodoList liste =  todoListRepository.findById(listId)
            .orElseThrow(() -> new TodoListNotFoundException("TodoList", "id", listId));
        
        if(!liste.getProprietaire().getId().equals(currentUserId) ){
            log.warn("L'utilisateur {} n'est pas le propriétaire de la liste {}", currentUserId, listId);
            throw new AccessDeniedException("Utilisateur", "currentUserId", currentUserId);
        }

        if(partageRepository.existsByTodoListIdAndInviteId(listId, invite.getId())){
            log.warn("L'utilisateur {} est déjà invité à collaborer sur la liste {}", request.inviteEmail(), listId);
            throw new AlreadySharedException("TodoList", "id", listId);
        }

        if(invite.getId().equals(currentUserId)
        
        ){
            log.warn("L'utilisateur {} ne peut pas s'inviter lui-même à collaborer sur la liste {}", request.inviteEmail(), listId);
            throw new SelfSharingException("TodoList", "id", listId);
        }
        Partage partage = Partage.builder()
            .todoList(liste)
            .invite(invite)
            .permission(request.permission())
            .build();
        
        Partage saved = partageRepository.save(partage);
        log.info("Partage créé avec succès entre l'utilisateur {} et l'invité {} sur la liste {}", currentUserId, request.inviteEmail(), listId);
        return partageMapper.toPartageResponse(saved);
    }

    @Override
    @Transactional
    public void revoquerPartage(Long listId, String inviteEmail, Long currentUserId) {
        log.info("Revocation de  l'utilisateur avec id : {} à l'invité avec email {} sur la liste de taches avec id : {}", listId, inviteEmail, currentUserId);
        Utilisateur invite = utilisateurRepository.findByEmail(inviteEmail)
            .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur", "email", inviteEmail));
        
        log.info("Utilisateur invité trouvé : {}", invite.getPseudo());
        log.info("Vérification des permissions de l'utilisateur {} pour la liste {}", currentUserId, listId);

        TodoList liste =  todoListRepository.findById(listId)
            .orElseThrow(() -> new TodoListNotFoundException("TodoList", "id", listId));
        
        if(!liste.getProprietaire().getId().equals(currentUserId)){
            log.warn("L'utilisateur {} n'est pas le propriétaire de la liste {}", currentUserId, listId);
            throw new AccessDeniedException("Utilisateur", "currentUserId", currentUserId);
        }

        Partage partage = partageRepository.findByTodoListIdAndInviteId(liste.getId(), invite.getId())
            .orElseThrow(() -> new PartageNotFoundException("Partage", "todoListId", listId));
        partageRepository.delete(partage);
        log.info("Partage révoqué avec succès entre l'utilisateur {} et l'invité {} sur la liste {}", currentUserId, inviteEmail, listId);
    }

    @Override
    @Transactional
    public Page<PartageResponse> getPartagesDeMaListe(Long listId, Long currentUserId, Pageable pageable) {
        log.info("Récupération des partages de la liste {} pour l'utilisateur {}", listId, currentUserId);
        TodoList liste =  todoListRepository.findById(listId)
            .orElseThrow(() -> new TodoListNotFoundException("TodoList", "id", listId));

        if(!liste.getProprietaire().getId().equals(currentUserId)){
            log.warn("L'utilisateur {} n'est pas le propriétaire de la liste {}", currentUserId, listId);
            throw new AccessDeniedException("Utilisateur", "currentUserId", currentUserId);
        }
        Page<Partage> partagesPage = partageRepository.findAllByTodoListIdAndTodoListProprietaireId(listId, currentUserId, pageable);
        return partagesPage.map(partageMapper::toPartageResponse);
    }
    
}
