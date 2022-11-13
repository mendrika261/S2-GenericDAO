package database.exception.object;

public class ObjectDatabaseException extends Exception {
    public ObjectDatabaseException(String detail) {
        super("ObjectDatabaseException: " + detail);
    }
}
