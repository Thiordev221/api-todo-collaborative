package sn.thiordev221.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.dto.requests.UtilisateurUpdateRequest;
import sn.thiordev221.app.dto.responses.UtilisateurResponse;
import sn.thiordev221.app.service.contrats.UtilisateurService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    @GetMapping("/utilisateurs")
    public ResponseEntity<Page<UtilisateurResponse>> getUtilisateurs(
        @RequestParam(required = false) String pseudo,
        @RequestParam(required = false) Boolean actif,
        @PageableDefault(size=20, sort="id") Pageable pageable
    ){
        if(pseudo != null ) {
            if(!pseudo.isBlank()) return ResponseEntity.ok(utilisateurService.getAllByPseudoContainingIgnoreCase(pseudo, pageable));
        }

        if(Boolean.TRUE.equals(actif)){
            return ResponseEntity.ok(utilisateurService.getAllByActifTrue(pageable));
        }

        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs(pageable));
    }

    @GetMapping("/utilisateurs/{id}")
    public ResponseEntity<UtilisateurResponse> getUtilisateurById(@PathVariable("id") Long id){
        return ResponseEntity.ok(utilisateurService.getUtilisateurById(id));
    }

    @PutMapping("/utilisateurs/{id}")
    public ResponseEntity<UtilisateurResponse> updateUtilisateur(
        @PathVariable("id") Long id,
        @RequestBody @Valid UtilisateurUpdateRequest request
    ){
        return ResponseEntity.ok(utilisateurService.updateUtilisateur(id, request));
    }

    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id){
        utilisateurService.deleteUtilisateur(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}
