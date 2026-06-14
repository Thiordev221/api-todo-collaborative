package sn.thiordev221.app.custom_exceptons;

public class TokenReplayException extends RuntimeException{

    public TokenReplayException(String message){
        super(message);
    }
    
}
