package sn.thiordev221.app.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import sn.thiordev221.app.dto.requests.TacheCreateRequest;
import sn.thiordev221.app.dto.requests.TacheUpdateRequest;
import sn.thiordev221.app.dto.responses.TacheResponse;
import sn.thiordev221.app.model.Tache;

@Mapper(componentModel="spring")
public interface TacheMapper {

    @Mapping(target = "id", ignore=true)
    @Mapping(target = "todoList", ignore=true)
    @Mapping(target = "termine", ignore=true)
    @Mapping(target = "dateCreation", ignore=true)
    Tache toTache(TacheCreateRequest request);

    @Mapping(target = "id", ignore=true)
    @Mapping(target = "todoList", ignore=true)
    @Mapping(target = "dateCreation", ignore=true)
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateTacheFromRequest(TacheUpdateRequest request, @MappingTarget Tache tache);

    @Mapping(target="todoListId", source="todoList.id")
    TacheResponse toTacheResponse(Tache tache);
}
