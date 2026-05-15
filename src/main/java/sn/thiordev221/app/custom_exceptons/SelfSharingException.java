package sn.thiordev221.app.custom_exceptons;

public class SelfSharingException  extends RuntimeException{
    public SelfSharingException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Impossible de partager %s avec soi-même : %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
