package database.provider;

import database.core.Affectation;
import database.core.DBConnection;
import database.core.Database;
import database.core.Sequence;
import database.exception.SQL.AttributeTypeNotExistingException;

import java.lang.reflect.Field;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Class that implements PostgreSQL in Java */
public class PostgreSQL extends Database {

    /** Constructor */
    public PostgreSQL(String host, String port, String dbName, String user, String password) {
        super(host, port, dbName, user, password);
    }

    /** Create a connection and return a DBConnection that contain the database and an instance of connection */
    @Override
    public DBConnection createConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception ignored) {} // Ignore problem with the javac classpath TODO Find solution
        String url = "jdbc:postgresql://"+getHost()+":"+getPort()+"/"+getDbName();
        DBConnection dbConnection = new DBConnection(this, DriverManager.getConnection(url, getUser(), getPassword()));
        /* Set autocommit off for transaction */
        dbConnection.setAutoCommit(false);
        return dbConnection;
    }

    /**  Create the function that we use to get a sequence */
    public String createSequenceFunctionSQL() {
        /* The signature of the function is getSequence(length int, prefix text, seq bigint) */
        return "CREATE OR REPLACE FUNCTION getSequence(length int, prefix text, seq bigint)" +
                "returns text" +
                "    language plpgsql" +
                "    as" +
                "    $$" +
                "BEGIN" +
                "    return prefix || lpad(CAST(seq as text), length, '0');" +
                "END;" +
                "$$";
    }

    /** List of corresponding sql type with java */
    @Override
    public String getSqlType(Field field) throws AttributeTypeNotExistingException {
        return switch (field.getType().getSimpleName()) {
            case "String" -> "TEXT";
            case "int" -> "INTEGER";
            case "double", "Double" -> "DOUBLE PRECISION";
            case "Date" -> "DATE";
            case "Timestamp" -> "TIMESTAMP";
            case "boolean" -> "BOOL";
            default -> throw new AttributeTypeNotExistingException(field, this);
        };
    }

    /** Create a table */
    @Override
    public String createTableSQL(String name, Field[] fields) throws AttributeTypeNotExistingException {
        /* Corresponding sql for create a table */
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append("\"").append(name).append("\"").append(" (");
        for(Field field: fields)
            sql.append("\"").append(field.getName()).append("\"").append(" ").append(getSqlType(field)).append(",");
        return sql.deleteCharAt(sql.lastIndexOf(",")).append(")").toString();
    }

    /** Create a sequence */
    @Override
    public String createSequenceSQL(String name) {
        return "CREATE SEQUENCE IF NOT EXISTS "+name+" START WITH 1 INCREMENT BY 1";
    }

    /** Insert query with preparedStatement */
    @Override
    public String insertSQL(String table, int valueLength) {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append("\"").append(table).append("\"").append(" VALUES(");
        for(int i=0; i<valueLength; i++) sql.append('?').append(",");
        return sql.deleteCharAt(sql.lastIndexOf(",")).append(")").toString();
    }

    /** Update query with preparedStatement */
    @Override
    public String updateSQL(String table, String condition, Affectation... affectations) {
        StringBuilder sql = new StringBuilder("UPDATE ").append("\"").append(table).append("\"").append(" SET ");
        for(Affectation affectation:affectations)
            sql.append("\"").append(affectation.getColumn()).append("\"").append(" ").append("=").append(" ").append("?").append(",");
        return sql.deleteCharAt(sql.lastIndexOf(",")).append(" WHERE ").append(condition).toString();
    }

    /** Delete query */
    @Override
    public String deleteSQL(String table, String condition) {
        return "DELETE FROM \"" + table + "\" WHERE " + condition;
    }

    /** Select respectively one column in a table */
    @Override
    public String selectSQLValue(String column, String table, String condition) {
        return "SELECT " + column + " AS result FROM \"" + table + "\" WHERE " + condition;
    }

    /** Select all column (to transform into object) */
    @Override
    public String selectSQLObject(String table, String condition) {
        return "SELECT * FROM \"" + table + "\" WHERE " + condition;
    }

    /** Get a sequence value using the getSequence function */
    @Override
    public String getSequenceSQL(Sequence sequence) {
        return "SELECT getSequence("+(sequence.getLength()-sequence.getPrefix().length())+", '"+sequence.getPrefix()+"', nextval('"+sequence.getSequenceName()+"')) AS result";
    }
}
