package org.example.Model;

import java.time.LocalDate;

public class Livre {
    protected int id;
    protected String titre;
    protected String auteur;
    protected int nbrPages;

    public Livre() {}

    public Livre(String titre, String auteur, int nbrPages) {
        this.titre = titre;
        this.auteur = auteur;
        this.nbrPages = nbrPages;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public int getNbrPages() { return nbrPages; }
    public void setNbrPages(int nbrPages) { this.nbrPages = nbrPages; }

    public void afficherDetails() {
        System.out.println("Titre: " + titre + ", Auteur: " + auteur + ", Pages: " + nbrPages);
    }
}