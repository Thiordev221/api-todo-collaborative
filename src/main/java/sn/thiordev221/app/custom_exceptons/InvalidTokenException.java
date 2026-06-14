package sn.thiordev221.app.custom_exceptons;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message){
        super(message);
    }
    
}
