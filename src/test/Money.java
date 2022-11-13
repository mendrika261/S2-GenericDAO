package test;


import database.core.DBObject;

public class Money extends DBObject {
    int value;
    String name;

    public Money() {
    }

    public Money(int value, String name) {
        setName(name);
        setValue(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
