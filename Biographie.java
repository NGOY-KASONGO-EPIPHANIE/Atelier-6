package org.example.Model;

import java.time.LocalDate;

public class Biographie extends Livre {
    private String sujet;
    private LocalDate datePublication;

    public Biographie() {}

    public Biographie(String titre, String auteur, int nbrPages, String sujet, LocalDate datePublication) {
        super(titre, auteur, nbrPages);
        this.sujet = sujet;
        this.datePublication = datePublication;
    }

    // Getters et setters
    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }
    public LocalDate getDatePublication() { return datePublication; }
    public void setDatePublication(LocalDate datePublication) { this.datePublication = datePublication; }

    @Override
    public void afficherDetails() {
        super.afficherDetails();
        System.out.println("Sujet: " + sujet + ", Date publication: " + datePublication);
    }
}