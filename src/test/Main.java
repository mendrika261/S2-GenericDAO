package test;

import database.core.DBConnection;
import database.core.Database;
import database.provider.PostgreSQL;
import test.Emp;
import test.Plat;
import test.PlatConso;


public class Main {
    public static void main(String[] args) throws Exception {
        Database database = new PostgreSQL("localhost", "5432", "dao", "", "");
        DBConnection dbConnection = database.createConnection();

        Emp emp = new Emp();
        //emp.createTable(dbConnection);

        Plat plat = new Plat();
        //plat.createTable(dbConnection);

        PlatConso platConso = new PlatConso();
        //platConso.createTable(dbConnection);

        Emp emp1 = new Emp("John", "Doe");
        emp1.save(dbConnection);
        Emp emp2 = new Emp("Jane", "Doe");
        emp2.save(dbConnection);
        Emp emp3 = new Emp("John", "Smith");
        emp3.save(dbConnection);

        Plat plat1 = new Plat("Poulet");
        plat1.save(dbConnection);
        Plat plat2 = new Plat("Boeuf");
        plat2.save(dbConnection);
        Plat plat3 = new Plat("Poisson");
        plat3.save(dbConnection);


        /* 1. Mi creer anle fonction sequence */
        // database.createSequenceFunction(dbConnection.getConnection());


        /* 2. Mi creer table */
        // Student student = new Student();
        // student.createTable(dbConnection);


        /* 3. exemple */

        // sauvegarder
        // Student student = new Student("mendrika", 120, LocalDateTime.now(), LocalTime.now(), LocalDate.now());
        // byte[] content = new byte[0];
        // student.setFile(content);
        // student.save(dbConnection);


        /* get by id */
        // Student student = new Student();
        // student = (Student) student.getById(dbConnection, "0000000001");
        // System.out.println(student.getBirthday());


        /* supprimer dans la table */
        // student.setId("0000000001");
        // student.delete(dbConnection);


        /* update dans la table */
        // student.setId("sequence id no eto");
        // student.update(dbConnection);


        /* select */
        // Student object = new Student();

        /* raha objet ray no alaina */
        // Student getOneObject = (Student) object.get(dbConnection, "condition no eto oh: mark>=10");

        /* raha objet maromaro no alaina */
        // object.getAll(dbConnection, "condition koa no eto"); // mamerina List<Object>
        // object.getAll(dbConnection); // raha maka ny objet rehetra tsisy condition*/

        dbConnection.commit();
        dbConnection.close();
    }
}
