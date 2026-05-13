package sn.thiordev221.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sn.thiordev221.app.dto.requests.TodoListRequest;
import sn.thiordev221.app.dto.responses.TodoListResponse;
import sn.thiordev221.app.model.TodoList;

@Mapper(componentModel="spring")
public interface TodoListMapper {
    
    @Mapping(target = "id", ignore=true)
    @Mapping(target = "dateCreation", ignore=true)
    @Mapping(target = "proprietaire", ignore=true)
    @Mapping(target = "taches", ignore=true)
    @Mapping(target = "partages", ignore=true)
    TodoList toTodoList(TodoListRequest request);

    @Mapping(target = "proprietaireId", source="proprietaire.id")
    @Mapping(target = "proprietairePseudo", source="proprietaire.pseudo")
    @Mapping(target = "nbTaches", expression="java(todoList.getTaches() != null ? todoList.getTaches().size() : 0)")
    @Mapping(target = "mesPermissions", ignore=true) // à calculer dans le service
    TodoListResponse toTodoListResponse(TodoList todoList);

}
