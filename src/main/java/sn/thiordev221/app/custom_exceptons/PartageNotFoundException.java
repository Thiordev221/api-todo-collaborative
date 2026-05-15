package sn.thiordev221.app.custom_exceptons;

public class PartageNotFoundException extends RuntimeException {
    public PartageNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s non trouvé avec %s : '%s'", resourceName, fieldName, fieldValue));
    }
}

