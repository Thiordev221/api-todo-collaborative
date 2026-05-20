package sn.thiordev221.app.service.implementations;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.model.TodoList;
import sn.thiordev221.app.repository.PartageRepository;

@Component
@RequiredArgsConstructor
public class PermissionHelper {
    private final PartageRepository partageRepository;

    public String calculatePermissions(TodoList list, Long userId) {
        if (list.getProprietaire().getId().equals(userId)) return "OWNER";
        return partageRepository.findByTodoListIdAndInviteId(list.getId(), userId)
            .map(p -> p.getPermission().name())
            .orElse("NONE");
    }
}
