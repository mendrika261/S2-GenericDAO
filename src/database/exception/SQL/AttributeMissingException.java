package database.exception.SQL;

public class AttributeMissingException extends DatabaseSQLException {
    public AttributeMissingException(Object object) {
        super("L'objet "+object.getClass().getSimpleName()+" doit au moins contenir un attribut");
    }
}
