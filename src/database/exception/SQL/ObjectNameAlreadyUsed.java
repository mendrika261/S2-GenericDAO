package database.exception.SQL;

public class ObjectNameAlreadyUsed extends DatabaseSQLException {
    public ObjectNameAlreadyUsed(String type, String name) {
        super("Il existe déjà un objet ("+type+") qui utilise le nom de: "+name);
    }
}
