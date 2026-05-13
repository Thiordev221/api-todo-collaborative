package sn.thiordev221.app.dto.responses;

import java.time.LocalDateTime;

import sn.thiordev221.app.model.Permission;

public record PartageResponse(
    Long todoListId,
    String todoListTitre,
    String inviteEmail,
    String invitePseudo,
    Permission permission,
    LocalDateTime datePartage
) {
    
}
