package sn.thiordev221.app.custom_exceptons;

public class AlreadySharedException  extends RuntimeException {
    public AlreadySharedException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s est déjà partagé avec %s : '%s'", resourceName, fieldName, fieldValue));
    }
    
}
