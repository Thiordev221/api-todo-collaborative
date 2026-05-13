package sn.thiordev221.app.dto.requests;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TacheCreateRequest(
    @NotBlank
    @Size(max = 150)
    String titre,

    @Size(max = 1000)
    String description,

    LocalDateTime echeance
) {
    
}
