package sn.thiordev221.app.controller;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.dto.requests.TodoListRequest;
import sn.thiordev221.app.dto.responses.TodoListResponse;
import sn.thiordev221.app.service.contrats.TodoListService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TodoListController {

    private final TodoListService todoListService;

    @PostMapping("/lists")
    public ResponseEntity<TodoListResponse> createTodoList(
        @RequestBody @Valid TodoListRequest request,
        @RequestHeader("X-User-Id") Long currentUserId
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(todoListService.createList(request, currentUserId));
    }

    @GetMapping("/lists/{id}")
    public ResponseEntity<TodoListResponse> getTodoListById(
        @PathVariable Long id,
        @RequestHeader("X-User-Id") Long currentUserId
    ){
        return ResponseEntity.ok(todoListService.getListById(id, currentUserId));
    }

    @GetMapping("/lists/my-lists")
    public ResponseEntity<Page<TodoListResponse>> getMyLists(
        @RequestHeader("X-User-Id") Long currentUserId, 
        @PageableDefault(size=20, sort="id") Pageable pageable
    ){
        return ResponseEntity.ok(todoListService.getMyLists(currentUserId, pageable));
    }
    
    @PutMapping("/lists/{listId}")
    public ResponseEntity<TodoListResponse> updateList(
        @PathVariable("listId") Long listId,
        @RequestBody @Valid TodoListRequest request,
        @RequestHeader("X-User-Id") Long currentUserId
    ){
        return ResponseEntity.ok(todoListService.updateList(listId, request, currentUserId));
    }

    @DeleteMapping("/lists/{listId}")
    public ResponseEntity<Void> deleteList(
        @PathVariable("listId") Long listId,
        @RequestHeader("X-User-Id") Long currentUserId
    ){
        todoListService.deleteList(listId, currentUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/lists/sharedByUser")
    public ResponseEntity<Page<TodoListResponse>> getAllListsCurrentUserShared(
        @RequestHeader("X-User-Id") Long currentUserId,
        @PageableDefault(size=20, sort="id") Pageable pageable
    ){
            return ResponseEntity.ok(todoListService.getAllListsCurrentUserShared(currentUserId, pageable));
    }

    @GetMapping("/lists/sharedToUser")
    public ResponseEntity<Page<TodoListResponse>>getAllListsSharedToCurrentUser(
        @RequestHeader("X-User-Id") Long currentUserId,
        @PageableDefault(size=20, sort="id") Pageable pageable
    ){
            return ResponseEntity.ok(todoListService.getAllListsSharedToCurrentUser(currentUserId, pageable));
    }

}
