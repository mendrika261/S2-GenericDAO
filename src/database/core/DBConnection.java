package database.core;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    Database database;
    Connection connection;

    public DBConnection(Database database, Connection connection) {
        setDatabase(database);
        setConnection(connection);
    }

    public void commit() throws SQLException {
        getConnection().commit();
    }

    public void rollback() throws SQLException {
        getConnection().rollback();
    }

    public void setAutoCommit(boolean state) throws SQLException {
        getConnection().setAutoCommit(state);
    }

    public void close() throws SQLException {
        getConnection().close();
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
