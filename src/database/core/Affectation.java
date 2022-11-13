package database.core;

public class Affectation {
    String column;
    Object value;

    public Affectation(String column, Object value) {
        setColumn(column);
        setValue(value);
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }


    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
