package database.core;

import database.provider.Oracle;
import database.provider.PostgreSQL;

/** Quick configuration shortcut */
public class Config {
    static Database ORACLE_DB = new Oracle("10.211.55.8", "1521", "orcl", "scott", "tiger");
    static Database PG_DB = new PostgreSQL("localhost", "5432", "postgres", "mendrika", "");


    public static Database getOracleDb() {
        return ORACLE_DB;
    }

    public static Database getPgDb() {
        return PG_DB;
    }
}
