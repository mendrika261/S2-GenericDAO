package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class DBObject {
    String id;

    public void createTable(DBConnection dbConnection) throws SQLException, AttributeTypeNotExistingException, AttributeMissingException {
        dbConnection.getDatabase().createTable(dbConnection.getConnection(), this);
        dbConnection.getDatabase().createSequence(dbConnection.getConnection(), getClass().getSimpleName()+"_seq");
    }

    public void save(DBConnection dbConnection, Sequence sequence) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        setId(dbConnection.getDatabase().getSequence(dbConnection.getConnection(), sequence));
        dbConnection.getDatabase().insertObject(dbConnection.getConnection(), this);
    }

    public void save(DBConnection dbConnection) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Sequence sequence = new Sequence("", 10, getClass().getSimpleName());
        save(dbConnection, sequence);
    }

    void update(DBConnection dbConnection, String condition) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        dbConnection.getDatabase().updateObject(dbConnection.getConnection(), condition, this);
    }

    public void update(DBConnection dbConnection) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NotIdentifiedInDatabaseException {
        update(dbConnection,"id='"+getId()+"'");
    }

    void delete(DBConnection dbConnection, String condition) throws SQLException {
        dbConnection.getDatabase().delete(dbConnection.getConnection(), getClass().getSimpleName(), condition);
    }

    public void delete(DBConnection dbConnection) throws SQLException, NotIdentifiedInDatabaseException {
        delete(dbConnection, "id='"+getId()+"'");
    }

    public List<Object> getAll(DBConnection dbConnection, String condition) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return dbConnection.getDatabase().selectListObject(dbConnection.getConnection(), getClass(), condition);
    }

    public List<Object> getAll(DBConnection dbConnection) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return getAll(dbConnection, "1=1");
    }

    public Object get(DBConnection dbConnection, String condition) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return dbConnection.getDatabase().selectObject(dbConnection.getConnection(), getClass(), condition);
    }

    public Object getById(DBConnection dbConnection, String id) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return get(dbConnection, "id='"+id+"'");
    }

    public void historize(DBConnection dbConnection, String action) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        dbConnection.getDatabase().insert(dbConnection.getConnection(), "history", Timestamp.from(Instant.now()), getClass().getSimpleName(), action, toString());
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() throws NotIdentifiedInDatabaseException {
        if(id == null) throw new NotIdentifiedInDatabaseException(this);
        return id;
    }
}
