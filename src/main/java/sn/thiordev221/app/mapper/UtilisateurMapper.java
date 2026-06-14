package sn.thiordev221.app.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import sn.thiordev221.app.dto.requests.RegisterRequest;
import sn.thiordev221.app.dto.requests.UtilisateurUpdateRequest;
import sn.thiordev221.app.dto.responses.UtilisateurResponse;
import sn.thiordev221.app.model.Utilisateur;

@Mapper(componentModel="spring")
public interface UtilisateurMapper {

    @Mapping(target = "id", ignore=true)
    @Mapping(target = "actif", ignore=true)
    @Mapping(target = "todoLists", ignore=true)
    @Mapping(target = "partages", ignore=true)
    @Mapping(target = "roles", ignore=true)
    @Mapping(target = "dateCreation", ignore=true)
    Utilisateur toUtilisateur(RegisterRequest registerRequest);


    @Mapping(target = "id", ignore=true)
    @Mapping(target = "partages", ignore=true)
    @Mapping(target = "actif", ignore=true)
    @Mapping(target = "roles", ignore=true)
    @Mapping(target = "todoLists", ignore=true)
    @Mapping(target = "email", ignore=true)
    @Mapping(target = "dateCreation", ignore=true)
    @Mapping(target = "authorities", ignore=true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUtilisateurFromRequest(UtilisateurUpdateRequest update, @MappingTarget Utilisateur utilisateur);

    UtilisateurResponse toResponse(Utilisateur utilisateur);

}
