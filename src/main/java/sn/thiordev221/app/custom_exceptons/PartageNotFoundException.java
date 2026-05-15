package sn.thiordev221.app.custom_exceptons;

public class PartageNotFoundException extends RuntimeException{
    public PartageNotFoundException(String message){
        super(message);
    }

    public PartageNotFoundException(String message, Throwable throwable){
        super(message, throwable);
    }
}
