package sn.thiordev221.app.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "L'Email est obligatoire")
    @Email(message = "L'Email doit être valide")
    String email,

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Mot de passe minimum 6 caractères")
    String password
) {
    
}
