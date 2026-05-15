package sn.thiordev221.app.custom_exceptons;

public class UtilisateurNotFoundException extends RuntimeException {
    public UtilisateurNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s non trouvé avec %s : '%s'", resourceName, fieldName, fieldValue));
    }
}

