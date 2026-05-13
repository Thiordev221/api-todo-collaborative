package sn.thiordev221.app.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import sn.thiordev221.app.model.Permission;

public record  PartageRequest(
    @NotBlank(message = "L'email de l'invité est obligatoire")
    @Email(message = "Format d'email invalide")
    String inviteEmail,

    @NotNull(message = "La permission est obligatoire (READ_ONLY ou READ_WRITE)")
    Permission permission
) {
    
}
