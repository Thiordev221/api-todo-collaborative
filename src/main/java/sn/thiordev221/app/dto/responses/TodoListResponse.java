package sn.thiordev221.app.dto.responses;

import java.time.LocalDateTime;

public record TodoListResponse(
    Long id,
    String titre,
    String description,
    LocalDateTime dateCreation,
    Long proprietaireId,
    String proprietairePseudo,
    int nbTaches,
    String mesPermissions 
) {
    
}
