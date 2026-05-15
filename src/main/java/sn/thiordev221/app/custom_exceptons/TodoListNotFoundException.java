package sn.thiordev221.app.custom_exceptons;

public class TodoListNotFoundException extends RuntimeException{
    public TodoListNotFoundException(String message){
        super(message);
    }
    public TodoListNotFoundException(String message, Throwable throwable){
        super(message, throwable);
    }
    
}
