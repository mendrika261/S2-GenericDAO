package database.exception.object;

public class NotIdentifiedInDatabaseException extends ObjectDatabaseException {
    public NotIdentifiedInDatabaseException(Object object) {
        super("L'objet "+object.getClass().getSimpleName()+" n'est pas encore dans la base");
    }
}
