package sn.thiordev221.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sn.thiordev221.app.dto.requests.LoginRequest;
import sn.thiordev221.app.dto.requests.RegisterRequest;
import sn.thiordev221.app.dto.responses.AuthResponse;
import sn.thiordev221.app.model.Utilisateur;

@Mapper(componentModel="spring")
public interface AuthMapper {
    @Mapping(target = "id", ignore=true)
    @Mapping(target = "partages", ignore=true)
    @Mapping(target = "actif", ignore=true)
    @Mapping(target = "roles", ignore=true)
    @Mapping(target = "todoLists", ignore=true)
    @Mapping(target = "pseudo", ignore=true)
    @Mapping(target = "dateCreation", ignore=true)
    Utilisateur toUtilisateur(LoginRequest loginRequest);

    @Mapping(target = "id", ignore=true)
    @Mapping(target = "partages", ignore=true)
    @Mapping(target = "actif", ignore=true)
    @Mapping(target = "roles", ignore=true)
    @Mapping(target = "todoLists", ignore=true)
    @Mapping(target = "dateCreation", ignore=true)
    Utilisateur toUtilisateurFromRegisterRequest(RegisterRequest registerRequest);

    @Mapping(target = "userId", source="id")
    @Mapping(target = "accessToken", ignore=true)
    @Mapping(target = "refreshToken", ignore=true)
    @Mapping(target = "tokenType", ignore=true)
    AuthResponse toAuthResponse(Utilisateur utilisateur);

}
