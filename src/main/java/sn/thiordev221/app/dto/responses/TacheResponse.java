package sn.thiordev221.app.dto.responses;

import java.time.LocalDateTime;

public record TacheResponse(
    Long id,
    String titre,
    String description,
    boolean termine,
    LocalDateTime dateCreation,
    LocalDateTime echeance,
    Long todoListId
){}

