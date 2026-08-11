package sn.thiordev221.app.service.implementations;//package sn.thiordev221.app.service.implementations;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import sn.thiordev221.app.custom_exceptons.AccessDeniedException;
//import sn.thiordev221.app.dto.requests.TodoListRequest;
//import sn.thiordev221.app.dto.responses.TodoListResponse;
//import sn.thiordev221.app.mapper.TodoListMapper;
//import sn.thiordev221.app.model.TodoList;
//import sn.thiordev221.app.model.Utilisateur;
//import sn.thiordev221.app.repository.TodoListRepository;
//import sn.thiordev221.app.repository.UtilisateurRepository;
//
//@ExtendWith(MockitoExtension.class)
//class TodoListServiceImplTest {
//
//    @Mock
//    private TodoListRepository todoListRepository;
//
//    @Mock
//    private TodoListMapper todoListMapper;
//
//    @Mock
//    private UtilisateurRepository utilisateurRepository;
//
//    @Mock
//    private PermissionHelper helper;
//
//    @InjectMocks
//    private TodoListServiceImpl todoListService;
//
//    @Test
//    void shouldCreateListAndReturnOwnerPermission() {
//        var owner = Utilisateur.builder()
//            .id(10L)
//            .email("owner@example.com")
//            .pseudo("owner")
//            .password("secret")
//            .actif(true)
//            .build();
//
//        var request = new TodoListRequest("Ma liste", "Description");
//        var entity = TodoList.builder()
//            .titre(request.titre())
//            .description(request.description())
//            .build();
//        var saved = TodoList.builder()
//            .id(100L)
//            .titre(request.titre())
//            .description(request.description())
//            .proprietaire(owner)
//            .build();
//        var response = new TodoListResponse(100L, request.titre(), request.description(), saved.getDateCreation(), 10L, "owner", 0, "OWNER");
//
//        when(utilisateurRepository.findById(10L)).thenReturn(Optional.of(owner));
//        when(todoListMapper.toTodoList(request)).thenReturn(entity);
//        when(todoListRepository.save(entity)).thenReturn(saved);
//        when(todoListMapper.toTodoListResponse(saved)).thenReturn(new TodoListResponse(saved.getId(), saved.getTitre(), saved.getDescription(), saved.getDateCreation(), owner.getId(), owner.getPseudo(), 0, null));
//
//        var result = todoListService.createList(request, 10L);
//
//        assertThat(result.id()).isEqualTo(100L);
//        assertThat(result.mesPermissions()).isEqualTo("OWNER");
//        assertThat(result.proprietaireId()).isEqualTo(10L);
//    }
//
//    @Test
//    void shouldThrowAccessDeniedWhenUserHasNoPermissionOnList() {
//        var list = TodoList.builder()
//            .id(50L)
//            .titre("List test")
//            .description("Desc")
//            .proprietaire(Utilisateur.builder().id(20L).build())
//            .build();
//
//        when(todoListRepository.findById(50L)).thenReturn(Optional.of(list));
//        when(helper.calculatePermissions(list, 999L)).thenReturn("NONE");
//
//        assertThrows(AccessDeniedException.class, () -> todoListService.getListById(50L, 999L));
//    }
//
//    @Test
//    void shouldReturnMyListsPage() {
//        var owner = Utilisateur.builder().id(10L).build();
//        var list = TodoList.builder().id(200L).titre("Liste A").proprietaire(owner).build();
//        var page = new PageImpl<>(java.util.List.of(list));
//
//        when(todoListRepository.findAllByProprietaireId(10L, PageRequest.of(0, 10))).thenReturn(page);
//        when(todoListMapper.toTodoListResponse(list)).thenReturn(new TodoListResponse(200L, "Liste A", null, list.getDateCreation(), 10L, null, 0, null));
//
//        var result = todoListService.getMyLists(10L, PageRequest.of(0, 10));
//
//        assertThat(result.getContent()).hasSize(1);
//        assertThat(result.getContent().get(0).id()).isEqualTo(200L);
//    }
//}

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import sn.thiordev221.app.custom_exceptons.AccessDeniedException;
import sn.thiordev221.app.custom_exceptons.TodoListNotFoundException;
import sn.thiordev221.app.dto.requests.TodoListRequest;
import sn.thiordev221.app.dto.responses.TodoListResponse;
import sn.thiordev221.app.mapper.TodoListMapper;
import sn.thiordev221.app.model.Role;
import sn.thiordev221.app.model.TodoList;
import sn.thiordev221.app.model.Utilisateur;
import sn.thiordev221.app.repository.TodoListRepository;
import sn.thiordev221.app.repository.UtilisateurRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoListServiceImplTest {

    @Mock
    private TodoListRepository todoListRepository;

    @Mock
    private TodoListMapper todoListMapper;;

    @Mock
    private UtilisateurRepository utilisateurRepository;;

    @Mock
    private PermissionHelper helper;

    @InjectMocks
    private TodoListServiceImpl todoListService;

    @Test
    void should_create_todo_list(){

        Long id = 1L;
        TodoListRequest req = new TodoListRequest("test", "test for this function");
        Utilisateur u = Utilisateur.builder().id(id).build();
        TodoList todoMapped = TodoList.builder().id(id).titre(req.titre()).description(req.description()).build();
        TodoList saved = TodoList.builder().id(id).titre(req.titre()).description(req.description()).proprietaire(u).build();
        TodoListResponse res = new TodoListResponse(id, saved.getTitre(), saved.getDescription(), saved.getDateCreation(), saved.getId(), null, 0, null);
        TodoListResponse res2 = new TodoListResponse(
                res.id(),
                res.titre(),
                res.description(),
                res.dateCreation(),
                res.proprietaireId(),
                res.proprietairePseudo(),
                res.nbTaches(),
                "OWNER"
        );
        when(utilisateurRepository.findById(id)).thenReturn(Optional.ofNullable(u));
        when(todoListMapper.toTodoList(req)).thenReturn(todoMapped);
        when(todoListRepository.save(todoMapped)).thenReturn(saved);
        when(todoListMapper.toTodoListResponse(saved)).thenReturn(res2);

        assertEquals(res2, todoListService.createList(req, id));

    }

    @Test
    void should_get_todo_list_by_id(){
        Long todoListId = 1L;
        Long userId = 1L;

        Utilisateur userFound = Utilisateur.builder()
                .email("abdoulaye@todo.sn")
                .password("pass123")
                .pseudo("Abdoulaye")
                .roles(Set.of(Role.ROLE_USER))
                .actif(true)
                .build();

        TodoList todoListFound = TodoList.builder().id(todoListId).titre("todo 1").description("desc of todo 1").build();
        TodoListResponse expected = new TodoListResponse(todoListFound.getId(), todoListFound.getTitre(), todoListFound.getDescription(), todoListFound.getDateCreation(), userFound.getId(), userFound.getPseudo(), 0, "OWNER");
        when(todoListRepository.findById(todoListId)).thenReturn(Optional.ofNullable(todoListFound));
        when(helper.calculatePermissions(todoListFound, userId)).thenReturn("OWNER");
        when(todoListMapper.toTodoListResponse(todoListFound)).thenReturn(expected);
        TodoListResponse actual = todoListService.getListById(todoListId, userId);

        assertEquals(actual, expected );
    }

    @Test
    void should_get_a_TodoListNotFoundException_by_trying_to_get_todo_list_by_id(){
        Long todoListId = 1L;
        Long userId = 1L;


        when(todoListRepository.findById(todoListId)).thenReturn(Optional.empty());

        Assertions.assertThrows(TodoListNotFoundException.class, () -> todoListService.getListById(todoListId, userId));
        verify(helper, never()).calculatePermissions(any(), any());
    }

    @Test
    void should_throw_AccessDeniesException_when_user_has_no_permission_on_list(){
        Long todoListId = 1L;
        Long userId = 1L;
        TodoList todoListFound = TodoList.builder().id(todoListId).titre("todo 1").description("desc of todo 1").build();

        when(todoListRepository.findById(todoListId)).thenReturn(Optional.of(todoListFound));
        when(helper.calculatePermissions(todoListFound, userId)).thenReturn("NONE");

        Assertions.assertThrows(AccessDeniedException.class, ()->todoListService.getListById(todoListId, userId));

        verify(todoListMapper, never()).toTodoListResponse(any());
    }

    @Test
    void should_update_todo_list(){
        Long userId = 1L;
        Long todoListId = 1L;
        TodoListRequest req = new TodoListRequest("test", "test for this function");
        Utilisateur proprietaire = Utilisateur.builder()
                .id(userId)
                .email("abdoulaye@todo.sn")
                .password("pass123")
                .pseudo("Abdoulaye")
                .roles(Set.of(Role.ROLE_USER))
                .actif(true)
                .build();
        TodoList todoListFound = TodoList.builder().id(todoListId).titre("todo 1").description("desc of todo 1").proprietaire(proprietaire).build();

        TodoListResponse expected = new TodoListResponse(
                todoListId, req.titre(), req.description(), null,
                proprietaire.getId(), proprietaire.getPseudo(), 0, "OWNER");

        when(todoListRepository.findById(todoListId)).thenReturn(Optional.of(todoListFound));
        when(todoListRepository.save(any(TodoList.class))).thenReturn(todoListFound);
        when(todoListMapper.toTodoListResponse(todoListFound)).thenReturn(expected);

        TodoListResponse actual = todoListService.updateList(todoListId, req, userId);

        // Vérifie que le VRAI objet mutable a bien été modifié par le service
        assertEquals(req.titre(), todoListFound.getTitre());
        assertEquals(req.description(), todoListFound.getDescription());
        assertEquals(expected, actual);
    }

    @Test
    void should_throw_TodoListNotFoundException_when_trying_to_update_a_non_existing_todo_list(){
        Long todoListId = 1L;
        Long userId = 1L;
        TodoListRequest req = new TodoListRequest("test", "test for this function");

        when(todoListRepository.findById(todoListId)).thenReturn(Optional.empty());

        assertThrows(TodoListNotFoundException.class, ()->todoListService.updateList(todoListId, req, userId));

        verify(todoListRepository, never()).save(any(TodoList.class));
    }

    @Test
    void should_throw_AccessDeniedException_when_user_have_no_permission_to_update_todo_list(){
        Long todoListId = 1L;
        Long userId = 1L;
        TodoListRequest req = new TodoListRequest("test", "test for this function");
        Utilisateur proprietaire = Utilisateur.builder()
                .id(2L)
                .email("abdoulaye@todo.sn")
                .password("pass123")
                .pseudo("Abdoulaye")
                .roles(Set.of(Role.ROLE_USER))
                .actif(true)
                .build();
        TodoList todoListFound = TodoList.builder().id(todoListId).titre("todo 1").description("desc of todo 1").proprietaire(proprietaire).build();

        when(todoListRepository.findById(todoListId)).thenReturn(Optional.of(todoListFound));

        assertThrows(AccessDeniedException.class, ()->todoListService.updateList(todoListId, req, userId));

        verify(todoListRepository, never()).save(any(TodoList.class));
    }

    @Test
    void should_delete_todo_list_by_id(){
        Long todoListId = 1L;
        Long userId = 1L;
        Utilisateur proprietaire = Utilisateur.builder()
                .id(userId)
                .email("abdoulaye@todo.sn")
                .password("pass123")
                .pseudo("Abdoulaye")
                .roles(Set.of(Role.ROLE_USER))
                .actif(true)
                .build();
        TodoList todoListFound = TodoList.builder().id(todoListId).titre("todo 1").description("desc of todo 1").proprietaire(proprietaire).build();

        when(todoListRepository.findById(todoListId)).thenReturn(Optional.of(todoListFound));

        todoListService.deleteList(todoListId, userId);

        verify(todoListRepository).delete(todoListFound);
    }
    @Test
    void should_throw_TodoListNotFoundException_when_trying_to_delete_a_non_existing_todo_list(){
        Long todoListId = 1L;
        Long userId = 1L;

        when(todoListRepository.findById(todoListId)).thenReturn(Optional.empty());

        assertThrows(TodoListNotFoundException.class, ()->todoListService.deleteList(todoListId, userId));

        verify(todoListRepository, never()).delete(any(TodoList.class));
    }

    @Test
    void should_throw_AccessDeniedException_when_user_have_no_permission_to_delete_todo_list(){
        Long todoListId = 1L;
        Long userId = 1L;
        Utilisateur proprietaire = Utilisateur.builder()
                .id(2L)
                .email("abdoulaye@todo.sn")
                .password("pass123")
                .pseudo("Abdoulaye")
                .roles(Set.of(Role.ROLE_USER))
                .actif(true)
                .build();
        TodoList todoListFound = TodoList.builder().id(todoListId).titre("todo 1").description("desc of todo 1").proprietaire(proprietaire).build();

        when(todoListRepository.findById(todoListId)).thenReturn(Optional.of(todoListFound));

        assertThrows(AccessDeniedException.class, ()->todoListService.deleteList(todoListId, userId));

        verify(todoListRepository, never()).delete(any(TodoList.class));
    }

    @Test
    void should_get_lists_for_a_user(){
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        Utilisateur proprietaire = Utilisateur.builder().id(userId).pseudo("Abdoulaye").build();
        TodoList todoListFound = TodoList.builder().id(1L).titre("todo 1").description("desc").proprietaire(proprietaire).build();

        Page<TodoList> todoListPage = new PageImpl<>(List.of(todoListFound), pageable, 1);


        TodoListResponse expected = new TodoListResponse(
                todoListFound.getId(), todoListFound.getTitre(), todoListFound.getDescription(),
                todoListFound.getDateCreation(), proprietaire.getId(), proprietaire.getPseudo(), 0, "OWNER");

        when(todoListRepository.findAllByProprietaireId(userId, pageable)).thenReturn(todoListPage);
        when(todoListMapper.toTodoListResponse(todoListFound)).thenReturn(expected);

        Page<TodoListResponse> result = todoListService.getMyLists(userId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(expected, result.getContent().get(0));
    }
}
