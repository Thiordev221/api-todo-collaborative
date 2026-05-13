package sn.thiordev221.app.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    String email,
    
    @NotBlank(message = "Le pseudo est obligatoire")
    @Size(min = 3, max = 50, message = "Pseudo entre 3 et 50 caractères")
    String pseudo,
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Mot de passe minimum 6 caractères")
    String password

) {
    }
