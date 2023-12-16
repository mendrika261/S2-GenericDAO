package test;

import database.core.GenericDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Student extends GenericDAO {
    String name;
    int mark;
    LocalDateTime birthday;
    LocalTime localTime;
    LocalDate localDate;
    byte[] file;

    public Student() {
    }

    public Student(String name, int mark, LocalDateTime birthday, LocalTime localTime, LocalDate localDate) {
        setName(name);
        setMark(mark);
        setBirthday(birthday);
        setLocalTime(localTime);
        setLocalDate(localDate);
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

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
