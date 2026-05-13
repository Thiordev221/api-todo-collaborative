package sn.thiordev221.app.dto.responses;

import java.time.LocalDateTime;
import java.util.List;

import sn.thiordev221.app.model.Role;

public record UtilisateurResponse(
    Long id,
    String email,
    String pseudo,
    List<Role> roles,
    boolean actif,
    LocalDateTime dateCreation
) {
    
}
