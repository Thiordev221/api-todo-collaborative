package sn.thiordev221.app.service.implementations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sn.thiordev221.app.custom_exceptons.AlreadySharedException;
import sn.thiordev221.app.custom_exceptons.SelfSharingException;
import sn.thiordev221.app.dto.requests.PartageRequest;
import sn.thiordev221.app.dto.responses.PartageResponse;
import sn.thiordev221.app.mapper.PartageMapper;
import sn.thiordev221.app.model.Partage;
import sn.thiordev221.app.model.Permission;
import sn.thiordev221.app.model.TodoList;
import sn.thiordev221.app.model.Utilisateur;
import sn.thiordev221.app.repository.PartageRepository;
import sn.thiordev221.app.repository.TodoListRepository;
import sn.thiordev221.app.repository.UtilisateurRepository;

@ExtendWith(MockitoExtension.class)
class PartageServiceImplTest {

    @Mock
    private PartageRepository partageRepository;

    @Mock
    private PartageMapper partageMapper;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private TodoListRepository todoListRepository;

    @InjectMocks
    private PartageServiceImpl partageService;

    @Test
    void inviterUtilisateur_success() {
        Long listId = 1L;
        Long ownerId = 10L;
        Utilisateur invite = Utilisateur.builder().id(20L).email("a@b.com").pseudo("inv").build();
        TodoList liste = new TodoList();
        liste.setId(listId);
        liste.setProprietaire(Utilisateur.builder().id(ownerId).build());

        PartageRequest req = new PartageRequest("a@b.com", Permission.READ_WRITE);
        Partage saved = Partage.builder().id(3L).invite(invite).todoList(liste).permission(Permission.READ_WRITE).build();
        PartageResponse resp = new PartageResponse(3L, listId, null, invite.getEmail(), invite.getPseudo(), Permission.READ_WRITE, saved.getDatePartage());

        when(utilisateurRepository.findByEmail(req.inviteEmail())).thenReturn(Optional.of(invite));
        when(todoListRepository.findById(listId)).thenReturn(Optional.of(liste));
        when(partageRepository.existsByTodoListIdAndInviteId(listId, invite.getId())).thenReturn(false);
        when(partageRepository.save(org.mockito.Mockito.any())).thenReturn(saved);
        when(partageMapper.toPartageResponse(saved)).thenReturn(resp);

        var result = partageService.inviterUtilisateur(listId, req, ownerId);

        assertThat(result).isEqualTo(resp);
    }

    @Test
    void inviterUtilisateur_selfSharing() {
        Long listId = 1L;
        Long ownerId = 10L;
        Utilisateur invite = Utilisateur.builder().id(ownerId).email("a@b.com").pseudo("inv").build();
        TodoList liste = new TodoList();
        liste.setId(listId);
        liste.setProprietaire(Utilisateur.builder().id(ownerId).build());

        PartageRequest req = new PartageRequest("a@b.com", Permission.READ_WRITE);

        when(utilisateurRepository.findByEmail(req.inviteEmail())).thenReturn(Optional.of(invite));
        when(todoListRepository.findById(listId)).thenReturn(Optional.of(liste));

        assertThrows(SelfSharingException.class, () -> partageService.inviterUtilisateur(listId, req, ownerId));
    }

    @Test
    void inviterUtilisateur_alreadyShared() {
        Long listId = 1L;
        Long ownerId = 10L;
        Utilisateur invite = Utilisateur.builder().id(20L).email("a@b.com").pseudo("inv").build();
        TodoList liste = new TodoList();
        liste.setId(listId);
        liste.setProprietaire(Utilisateur.builder().id(ownerId).build());

        PartageRequest req = new PartageRequest("a@b.com", Permission.READ_WRITE);

        when(utilisateurRepository.findByEmail(req.inviteEmail())).thenReturn(Optional.of(invite));
        when(todoListRepository.findById(listId)).thenReturn(Optional.of(liste));
        when(partageRepository.existsByTodoListIdAndInviteId(listId, invite.getId())).thenReturn(true);

        assertThrows(AlreadySharedException.class, () -> partageService.inviterUtilisateur(listId, req, ownerId));
    }
}
