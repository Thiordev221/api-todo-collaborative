package sn.thiordev221.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.dto.requests.PartageRequest;
import sn.thiordev221.app.dto.responses.PartageResponse;
import sn.thiordev221.app.service.contrats.PartageService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PartageController {
    
    private final PartageService partageService;

    @PostMapping("/lists/{listId}/partages")
    public ResponseEntity<PartageResponse> inviterUtilisateur(
        @PathVariable("listId") Long listId, 
        @RequestBody @Valid PartageRequest request, 
        @RequestHeader("X-User-Id") Long currentUserId
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(partageService.inviterUtilisateur(listId, request, currentUserId));
    }

    @DeleteMapping("/lists/{listId}/partages")
    public ResponseEntity<Void> revoquerPartage(
        @PathVariable("listId") Long listId, 
        @RequestParam String inviteEmail, 
        @RequestHeader("X-User-Id") Long currentUserId
    ){
        partageService.revoquerPartage(listId, inviteEmail, currentUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/lists/{listId}/partages")
    public ResponseEntity<Page<PartageResponse>> getPartagesDeMaListe(
        @PathVariable("listId") Long listId, 
        @RequestHeader("X-User-Id") Long currentUserId, 
        @PageableDefault(size=20, sort="id") Pageable pageable
    ){
        return ResponseEntity.ok(partageService.getPartagesDeMaListe(listId, currentUserId, pageable)); 
    }

}
