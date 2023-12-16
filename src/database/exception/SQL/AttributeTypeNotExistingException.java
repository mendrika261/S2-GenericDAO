package database.exception.SQL;

import java.lang.reflect.Field;

public class AttributeTypeNotExistingException extends DatabaseSQLException {
    public AttributeTypeNotExistingException(Field field) {
        super("Le type "+field.getType().getSimpleName()+" n'a pas de correspondance dans le SGBD ");
    }
}
