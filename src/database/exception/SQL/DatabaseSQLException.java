package database.exception.SQL;

public class DatabaseSQLException extends Exception {
    public DatabaseSQLException(String detail) {
        super("DatabaseSQLException: " + detail);
    }
}
