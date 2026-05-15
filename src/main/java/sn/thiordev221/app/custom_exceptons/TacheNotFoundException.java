package sn.thiordev221.app.custom_exceptons;

public class TacheNotFoundException extends RuntimeException{
    public TacheNotFoundException(String message){
        super(message);
    }

    public TacheNotFoundException(String message, Throwable throwable){
        super(message, throwable);
    }
}
