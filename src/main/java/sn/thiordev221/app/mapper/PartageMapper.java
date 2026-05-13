package sn.thiordev221.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sn.thiordev221.app.dto.requests.PartageRequest;
import sn.thiordev221.app.dto.responses.PartageResponse;
import sn.thiordev221.app.model.Partage;

@Mapper(componentModel="spring")
public interface PartageMapper {
    
    @Mapping(target = "id", ignore=true)
    @Mapping(target = "todoList", ignore=true)
    @Mapping(target = "invite", ignore=true)
    @Mapping(target="datePartage", ignore=true)
    Partage toPartage(PartageRequest request);

    @Mapping(target = "todoListId", source="todoList.id")
    @Mapping(target = "todoListTitre", source="todoList.titre")
    @Mapping(target = "inviteEmail", source="invite.email")
    @Mapping(target = "invitePseudo", source="invite.pseudo")
    PartageResponse toPartageResponse(Partage partage);
}
