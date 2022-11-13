package database.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DBTool {
    public static String upperFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    /** Get all fields in the superclass and the object */
    public static Field[] getFieldWithSuperclass(Object object) {
        List<Field> fieldList = new ArrayList<>(List.of(object.getClass().getSuperclass().getDeclaredFields()));
        fieldList.addAll(List.of(object.getClass().getDeclaredFields()));
        return fieldList.toArray(new Field[0]);
    }
}
