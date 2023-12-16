package test;

import database.core.GenericDAO;

public class Plat extends GenericDAO {
    String libelle;

    public Plat() {
    }

    public Plat(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
