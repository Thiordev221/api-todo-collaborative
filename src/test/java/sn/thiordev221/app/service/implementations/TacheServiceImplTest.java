package sn.thiordev221.app.service.implementations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import sn.thiordev221.app.custom_exceptons.InvalidPermissionException;
import sn.thiordev221.app.custom_exceptons.TodoListNotFoundException;
import sn.thiordev221.app.dto.requests.TacheCreateRequest;
import sn.thiordev221.app.dto.responses.TacheResponse;
import sn.thiordev221.app.mapper.TacheMapper;
import sn.thiordev221.app.model.Tache;
import sn.thiordev221.app.model.TodoList;
import sn.thiordev221.app.repository.TacheRepository;
import sn.thiordev221.app.repository.TodoListRepository;

@ExtendWith(MockitoExtension.class)
class TacheServiceImplTest {

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private TacheMapper tacheMapper;

    @Mock
    private TodoListRepository todoListRepository;

    @Mock
    private PermissionHelper helper;

    @InjectMocks
    private TacheServiceImpl tacheService;

    @Test
    void createTache_success() {
        Long listId = 1L;
        Long userId = 2L;

        TodoList list = new TodoList();
        list.setId(listId);

        TacheCreateRequest req = new TacheCreateRequest("titre", "desc", null);
        Tache mapped = new Tache();
        Tache saved = new Tache();
        saved.setId(5L);
        TacheResponse response = new TacheResponse(5L, "titre", "desc", false, null, LocalDateTime.now(), 1L);

        when(todoListRepository.findById(listId)).thenReturn(Optional.of(list));
        when(helper.calculatePermissions(list, userId)).thenReturn("OWNER");
        when(tacheMapper.toTache(req)).thenReturn(mapped);
        when(tacheRepository.save(mapped)).thenReturn(saved);
        when(tacheMapper.toTacheResponse(saved)).thenReturn(response);

        var result = tacheService.createTache(listId, req, userId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void createTache_invalidPermission() {
        Long listId = 1L;
        Long userId = 2L;

        TodoList list = new TodoList();
        list.setId(listId);

        when(todoListRepository.findById(listId)).thenReturn(Optional.of(list));
        when(helper.calculatePermissions(list, userId)).thenReturn("ONLY_READ");
        TacheCreateRequest request = new TacheCreateRequest("t", "d", null);
        assertThrows(InvalidPermissionException.class, () -> tacheService.createTache(listId, request, userId));
    }

    @Test
    void getTachesDeLaListe_notFound() {
        when(todoListRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TodoListNotFoundException.class, () -> tacheService.getTachesDeLaListe(99L, 1L, PageRequest.of(0, 10)));
    }
}
