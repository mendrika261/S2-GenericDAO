package display;

import database.core.*;
import test.Student;


public class Main {
    public static void main(String[] args) throws Exception {
        Database database = Config.getPgDb();
        DBConnection dbConnection = database.createConnection();

        /* objet de test */
        Student student = new Student("mendrika", 100);

        // creer la table student
        student.createTable(dbConnection);

        // sauvegarder dans la table
        student.save(dbConnection);

        // supprimer dans la table
        student.setId("sequence id no eto");
        student.delete(dbConnection);

        // update dans la table
        student.setId("sequence id no eto");
        student.update(dbConnection);

        // select
        Student object = new Student();
        // raha objet ray no alaina
        Student getOneObject = (Student) object.get(dbConnection, "condition no eto oh: mark>=10");
        // raha objet maromaro no alaina
        object.getAll(dbConnection, "condition koa no eto"); // mamerina List<Object>
        object.getAll(dbConnection); // raha maka ny objet rehetra tsisy condition

        dbConnection.commit();
        dbConnection.close();
    }
}
