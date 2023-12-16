package test;

import database.core.GenericDAO;

public class Emp extends GenericDAO {
    String nom;
    String prenom;

    public Emp(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public Emp() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
