package sn.thiordev221.app.custom_exceptons;

public class InvalidPermissionException  extends RuntimeException {
    public InvalidPermissionException(Long listId, String requiredPermission) {
        super(String.format("Accès refusé pour la liste %d. Action requise : %s", listId, requiredPermission));
    }
    
}
