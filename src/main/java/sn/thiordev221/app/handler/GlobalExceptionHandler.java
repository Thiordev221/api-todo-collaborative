package sn.thiordev221.app.handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import sn.thiordev221.app.custom_exceptons.AccessDeniedException;
import sn.thiordev221.app.custom_exceptons.AlreadySharedException;
import sn.thiordev221.app.custom_exceptons.InvalidPermissionException;
import sn.thiordev221.app.custom_exceptons.InvalidTokenException;
import sn.thiordev221.app.custom_exceptons.PartageNotFoundException;
import sn.thiordev221.app.custom_exceptons.SelfSharingException;
import sn.thiordev221.app.custom_exceptons.TacheNotFoundException;
import sn.thiordev221.app.custom_exceptons.TodoListNotFoundException;
import sn.thiordev221.app.custom_exceptons.TokenReplayException;
import sn.thiordev221.app.custom_exceptons.UtilisateurConflictException;
import sn.thiordev221.app.custom_exceptons.UtilisateurNotFoundException;
import sn.thiordev221.app.dto.responses.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    //1. Gestion des exceptions de type "Not Found"
    @ExceptionHandler({
        UtilisateurNotFoundException.class,
        TodoListNotFoundException.class,
        TacheNotFoundException.class,
        PartageNotFoundException.class,
        InvalidTokenException.class
    })
    public ResponseEntity<ErrorResponse> handleNottFOundException(RuntimeException ex, WebRequest  request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    //2. Gestion des exceptions d'accès refusé
    @ExceptionHandler({
        AccessDeniedException.class,
        InvalidPermissionException.class
    })
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(RuntimeException ex, WebRequest  request) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    // 3. Gestion des conflits (409)
    @ExceptionHandler({
        UtilisateurConflictException.class,
        AlreadySharedException.class,
        SelfSharingException.class,
        TokenReplayException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    //Gestion des exceptions de validation (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(MethodArgumentNotValidException ex, WebRequest request){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false),
            errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //5. Gestion des exceptions de type "Internal Server Error"
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Internal Server Error",
        "Une erreur inattendue est survenue sur le serveur.", // Message sécurisé
        request.getDescription(false), // On récupère le path quand même
        null
    );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(RuntimeException ex, HttpStatus status,WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false),
            null
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}
