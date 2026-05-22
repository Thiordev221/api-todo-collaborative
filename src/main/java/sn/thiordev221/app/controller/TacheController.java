package sn.thiordev221.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.dto.requests.TacheCreateRequest;
import sn.thiordev221.app.dto.requests.TacheUpdateRequest;
import sn.thiordev221.app.dto.responses.TacheResponse;
import sn.thiordev221.app.service.contrats.TacheService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TacheController {
    
    private final TacheService tacheService;

    @PostMapping("/lists/{listId}/taches")
    public ResponseEntity<TacheResponse> ajouterTache(
        @PathVariable("listId") Long listId,
        @RequestBody @Valid TacheCreateRequest request, 
        @RequestHeader("X-User-Id") Long currentUserId
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(tacheService.createTache(listId, request, currentUserId));
    }

    @PutMapping("/lists/{listId}/taches/{tacheId}")
    public ResponseEntity<TacheResponse> modifierTache(
        @PathVariable("listId") Long listId, 
        @PathVariable("tacheId") Long tacheId, 
        @RequestBody @Valid TacheUpdateRequest request, 
        @RequestHeader("X-User-Id") Long currentUserId
    ){
        return ResponseEntity.ok(tacheService.updateTache(listId, tacheId, request, currentUserId));
    }

    @DeleteMapping("/lists/{listId}/taches/{tacheId}")
    public ResponseEntity<Void> deleteTache(
        @PathVariable("listId") Long listId,
        @PathVariable("tacheId") Long tacheId, 
        @RequestHeader("X-User-Id") Long currentUserId
    ){
        tacheService.deleteTache(listId, tacheId, currentUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/lists/{listId}/taches")
    public ResponseEntity<Page<TacheResponse>> getTachesDeLaListe(
        @PathVariable("listId") Long listId, 
        @RequestHeader("X-User-Id") Long currentUserId, 
        @PageableDefault(size=20, sort="id") Pageable pageable
    ){
        return ResponseEntity.ok(tacheService.getTachesDeLaListe(listId, currentUserId, pageable));
    }

    @PatchMapping("/lists/{listId}/taches/{tacheId}/toggleStatus")
    public ResponseEntity<TacheResponse> toggleStatus(
        @PathVariable("listId") Long listId,
        @PathVariable("tacheId") Long tacheId, 
        @RequestHeader("X-User-Id") Long currentUserId
    ){
        return ResponseEntity.ok(tacheService.toggleStatus(listId, tacheId, currentUserId));
    }

    @GetMapping("/lists/{listId}/taches/{tacheId}")
    public ResponseEntity<TacheResponse> getTacheById(
        @PathVariable("tacheId") Long tacheId,
        @PathVariable("listId") Long listId, 
        @RequestHeader("X-User-Id") Long currentUserId
    ){
        return ResponseEntity.ok(tacheService.getTacheById(tacheId, listId, currentUserId));
    }

}
