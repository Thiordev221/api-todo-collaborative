package sn.thiordev221.app.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import sn.thiordev221.app.custom_exceptons.PartageNotFoundException;
import sn.thiordev221.app.custom_exceptons.TacheNotFoundException;
import sn.thiordev221.app.custom_exceptons.TodoListNotFoundException;
import sn.thiordev221.app.custom_exceptons.UtilisateurNotFoundException;
import sn.thiordev221.app.dto.responses.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler({
        UtilisateurNotFoundException.class,
        TodoListNotFoundException.class,
        TacheNotFoundException.class,
        PartageNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNottFOundException(RuntimeException ex){
        
        return null;
    }
}
