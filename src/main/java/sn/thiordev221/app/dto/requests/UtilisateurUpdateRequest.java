package sn.thiordev221.app.dto.requests;

import jakarta.validation.constraints.Size;

public record UtilisateurUpdateRequest(
    @Size(min = 3, max = 50, message = "Le pseudo doit faire entre 3 et 50 caractères")
    String pseudo,

    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    String password
) {
    
}
