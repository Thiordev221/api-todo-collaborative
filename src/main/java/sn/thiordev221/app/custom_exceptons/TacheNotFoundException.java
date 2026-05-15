package sn.thiordev221.app.custom_exceptons;

public class TacheNotFoundException extends RuntimeException {
    public TacheNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s non trouvé avec %s : '%s'", resourceName, fieldName, fieldValue));
    }
}

