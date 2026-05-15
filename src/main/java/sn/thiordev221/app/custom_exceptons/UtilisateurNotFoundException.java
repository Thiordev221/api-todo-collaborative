package sn.thiordev221.app.custom_exceptons;

public class UtilisateurNotFoundException extends RuntimeException{
    public UtilisateurNotFoundException(String message){
        super(message);
    }
    public UtilisateurNotFoundException(String message, Throwable throwable){
        super(message, throwable);
    }
}
