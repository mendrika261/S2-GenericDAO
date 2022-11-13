package display;

import database.core.*;
import test.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Database database = Config.getPgDb();
        DBConnection dbConnection = database.createConnection();

        Student student = new Student("azerty", 321);
        student.createTable(dbConnection);
        Student student1 = (Student) student.getById(dbConnection, "TEST000003");
        student.save(dbConnection);

        List<Object> list = student.getAll(dbConnection);
        for(Object object:list) {
            System.out.println(((Student)object).getId());
            System.out.println(((Student)object).getName());
            System.out.println(((Student)object).getMark());
        }

        dbConnection.commit();
        dbConnection.close();
    }
}
