package test;

import database.core.DBObject;

public class Student extends DBObject {
    String name;
    int mark;

    public Student() {
    }

    public Student(String name, int mark) {
        setName(name);
        setMark(mark);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
