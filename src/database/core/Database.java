package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Database {
    String host;
    String port;
    String dbName;
    String user;
    String password;


    /** Constructor */
    public Database(String host, String port, String dbName, String user, String password) {
        setHost(host);
        setPort(port);
        setDbName(dbName);
        setUser(user);
        setPassword(password);
    }


    /** Abstract methods
    *   Create a connection and return a DBConnection that contain the database and an instance of connection */
    public abstract DBConnection createConnection() throws SQLException;
    /**  Create the function that we use to get a sequence */
    public abstract String createSequenceFunctionSQL();
    /** List of corresponding sql type with java */
    public abstract String getSqlType(Field field) throws AttributeTypeNotExistingException;
    /** Create a table */
    public abstract String createTableSQL(String name, Field[] fields) throws AttributeTypeNotExistingException;
    /** Create a sequence */
    public abstract String createSequenceSQL(String name);
    /** Insert query with preparedStatement */
    public abstract String insertSQL(String table, int valueLength);
    /** Update query with preparedStatement */
    public abstract String updateSQL(String table, String condition, Affectation... affectations);
    /** Delete query */
    public abstract String deleteSQL(String table, String condition);
    /** Select respectively one column in a table */
    public abstract String selectSQLValue(String column, String table, String condition);
    /** Select all column (to transform into object) */
    public abstract String selectSQLObject(String table, String condition);
    /** Get a sequence value using the getSequence function */
    public abstract String getSequenceSQL(Sequence sequence);


    /** Operations */
    public void execute(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }


    public void createSequenceFunction(Connection connection) throws SQLException {
        execute(connection, createSequenceFunctionSQL());
    }

    public void createTable(Connection connection, Object object) throws SQLException, AttributeTypeNotExistingException, AttributeMissingException {
        /* Get all fields in the superclass and the object */
        Field[] fields = DBTool.getFieldWithSuperclass(object);
        if(fields.length <= 1) throw new AttributeMissingException(object);

        execute(connection, createTableSQL(object.getClass().getSimpleName(), fields));
    }

    public void createSequence(Connection connection, String name) throws SQLException {
        execute(connection, createSequenceSQL(name));
    }

    public void insert(Connection connection, String table, Object... objects) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL(table, objects.length));

        for(int i=0; i<objects.length; i++) {
            if(objects[i] == null) objects[i] = "";
            if(objects[i].getClass() == Integer.class) {
                preparedStatement.setInt(i+1, (int) objects[i]);
            } else if (objects[i].getClass() == Double.class) {
                preparedStatement.setDouble(i+1, (double) objects[i]);
            } else {
                Method statementSetter = PreparedStatement.class.getDeclaredMethod("set" + objects[i].getClass().getSimpleName(), int.class, objects[i].getClass());
                statementSetter.invoke(preparedStatement, i+1, objects[i]);
            }
        }

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void insertObject(Connection connection, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, SQLException {
        List<Object> attribute = new ArrayList<>();

        for(Field field: object.getClass().getSuperclass().getDeclaredFields()) {
            Method getter = object.getClass().getSuperclass().getMethod("get"+ DBTool.upperFirst(field.getName()));
            attribute.add(getter.invoke(object));
        }

        for(Field field: object.getClass().getDeclaredFields()) {
            Method getter = object.getClass().getMethod("get"+ DBTool.upperFirst(field.getName()));
            attribute.add(getter.invoke(object));
        }

        insert(connection, object.getClass().getSimpleName(), attribute.toArray(new Object[0]));
    }

    public void update(Connection connection, String table, String condition, Affectation... affectations) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PreparedStatement preparedStatement = connection.prepareStatement(updateSQL(table, condition, affectations));

        for(int i=0; i<affectations.length; i++) {
            if(affectations[i].getValue().getClass() == Integer.class) {
                preparedStatement.setInt(i+1, (int) affectations[i].getValue());
            } else if (affectations[i].getValue().getClass() == Double.class) {
                preparedStatement.setDouble(i+1, (double) affectations[i].getValue());
            } else {
                Method statementSetter = PreparedStatement.class.getDeclaredMethod("set" + affectations[i].getValue().getClass().getSimpleName(), int.class, affectations[i].getValue().getClass());
                statementSetter.invoke(preparedStatement, i+1, affectations[i].getValue());
            }
        }

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void updateObject(Connection connection, String condition, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, SQLException {
        List<Affectation> affectationList = new ArrayList<>();
        for(Field field: object.getClass().getDeclaredFields()) {
            Method getterObject = object.getClass().getMethod("get"+ DBTool.upperFirst(field.getName()));
            affectationList.add(new Affectation(field.getName(), getterObject.invoke(object)));
        }

        update(connection, object.getClass().getSimpleName(), condition, affectationList.toArray(new Affectation[0]));
    }

    public void delete(Connection connection, String table, String condition) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(deleteSQL(table, condition));
        statement.close();
    }

    public String selectValue(Connection connection, String column, String table, String condition) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSQLValue(column, table, condition));
        String result = null;
        if(resultSet.next()) result = resultSet.getString(1);
        statement.close();
        return result;
    }

    public String selectValue(Connection connection, String column, String table) throws SQLException {
        return selectValue(connection, column, table, "1=1");
    }

    public List<Object> selectListObject(Connection connection, Class<?> object, String condition, int limit) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSQLObject(object.getSimpleName(), condition));

        List<Object> results = new ArrayList<>();
        Field[] fields = DBTool.getFieldWithSuperclass(object.getConstructor().newInstance());
        int limitCount = 0;

        while(resultSet.next() && limitCount!=limit) {
            Object newObject = object.getConstructor().newInstance();
            for (Field field : fields) {
                Method objectSetter;
                try {
                    objectSetter = object.getSuperclass().getDeclaredMethod("set" + DBTool.upperFirst(field.getName()), field.getType());
                } catch (NoSuchMethodException ignored) {
                    objectSetter = object.getDeclaredMethod("set" + DBTool.upperFirst(field.getName()), field.getType());
                }
                Method resultGetType = ResultSet.class.getDeclaredMethod("get"+ DBTool.upperFirst(field.getType().getSimpleName()), String.class);
                objectSetter.invoke(newObject, resultGetType.invoke(resultSet, field.getName()));
            }
            results.add(newObject);
            limitCount++;
        }

        statement.close();
        return results;
    }

    public List<Object> selectListObject(Connection connection, Class<?> object, String condition) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return selectListObject(connection, object, condition, -1);
    }

    public Object selectObject(Connection connection,  Class<?> object, String condition) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Object> objectList = selectListObject(connection, object, condition, 1);
        if(objectList.isEmpty()) return null;
        return objectList.get(0);
    }

    public String getSequence(Connection connection, Sequence sequence) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getSequenceSQL(sequence));
        String result = null;
        if(resultSet.next()) result = resultSet.getString(1);
        statement.close();
        return result;
    }


    /** Setters and getters */
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
