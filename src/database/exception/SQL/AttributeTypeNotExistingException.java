package database.exception.SQL;

import database.core.Database;

import java.lang.reflect.Field;

public class AttributeTypeNotExistingException extends DatabaseSQLException {
    public AttributeTypeNotExistingException(Field field, Database database) {
        super("Le type "+field.getType().getSimpleName()+" n'a pas de correspondance dans le SGBD "+database.getClass().getSimpleName());
    }
}
