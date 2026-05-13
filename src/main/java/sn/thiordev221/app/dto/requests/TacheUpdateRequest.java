package sn.thiordev221.app.dto.requests;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TacheUpdateRequest(
    @NotBlank(message = "Le titre ne peut pas être vide")
    @Size(max = 150)
    String titre,

    @Size(max = 1000)
    String description,

    boolean termine,
    
    LocalDateTime echeance
) {
    
}
