package sn.thiordev221.app.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "Le pseudo est obligatoire")
    @Size(min = 3, max = 30, message = "Le pseudo doit faire entre 3 et 30 caractères")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
             message = "Le pseudo ne peut contenir que des lettres, chiffres et underscores")
    String pseudo,

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    String email,

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit faire au moins 8 caractères")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
        message = "Le mot de passe doit contenir majuscule, minuscule, chiffre et caractère spécial"
    )
    String password
) {}
