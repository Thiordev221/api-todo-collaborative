package sn.thiordev221.app.custom_exceptons;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Access refusé pour %s avec %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
