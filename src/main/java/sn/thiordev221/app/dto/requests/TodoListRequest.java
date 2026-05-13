package sn.thiordev221.app.dto.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoListRequest(
    @NotBlank(message = "Le titre ne peut pas être vide")
    @Size(max = 100)
    String titre,

    @Size(max = 500)
    String description){
}
