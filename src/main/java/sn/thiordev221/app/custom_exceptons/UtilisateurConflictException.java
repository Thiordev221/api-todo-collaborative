package sn.thiordev221.app.custom_exceptons;

public class UtilisateurConflictException extends RuntimeException{
    public UtilisateurConflictException(String message){
        super(message);
    }
    public UtilisateurConflictException(String message, Throwable throwable){
        super(message, throwable);
    }
}
