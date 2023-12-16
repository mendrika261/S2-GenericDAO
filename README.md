## What is this?
This is a simple DAO for Java with some more features.
I used it for my school project.

## Features
- Create tables
- Crud for models
- Specific query into into `Object` or `List`
- Specific sequence like: SEC001, SEC002, ...
- Compatible with PostgreSQL and Oracle

## Overview
### Example of a model file
```java
public class Unite extends GenericDAO {
    String nom;

    public Unite() {
    }

    public Unite(String nom) throws ValidationException {
        setNom(nom);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) throws ValidationException {
        if (nom == null || nom.isEmpty())
            throw new ValidationException("Le nom de l'unité ne peut pas être vide");
        this.nom = nom;
    }
}
```

### Example of a migration file
```java
public class DaoConfig {
    public static final Database DATABASE = new PostgreSQL("localhost", "5432", "dao", "", "");

    public static void main(String[] args) throws SQLException, ValidationException, AttributeTypeNotExistingException, AttributeMissingException {
        DBConnection dbConnection = DATABASE.createConnection();

        createTables(dbConnection);

        dbConnection.commit();
        dbConnection.close();
    }

    public static void createTables(DBConnection dbConnection) throws SQLException, AttributeTypeNotExistingException, AttributeMissingException {
        dbConnection.getDatabase().createSequenceFunction(dbConnection.getConnection());

        Unite unite = new Unite();
        unite.createTable(dbConnection);
    }
}
```

### Example of a test file
See inside the `test` folder