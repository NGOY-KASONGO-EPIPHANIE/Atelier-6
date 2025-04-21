package org.example.Model;

import org.example.BiographieInterface;

import java.time.LocalDate;

public class Biographie extends Livre implements BiographieInterface {
    private String sujet;
    private LocalDate datePublication;

    public Biographie() {}

    public Biographie(String titre, String auteur, int nbrPages, String sujet, LocalDate datePublication) {
        super(titre, auteur, nbrPages);
        this.sujet = sujet;
        this.datePublication = datePublication;
    }

    // Getters et setters
    @Override
    public String getSujet() { return sujet; }
    @Override
    public void setSujet(String sujet) { this.sujet = sujet; }
    @Override
    public LocalDate getDatePublication() { return datePublication; }
    @Override
    public void setDatePublication(LocalDate datePublication) { this.datePublication = datePublication; }

    @Override
    public void afficherDetails() {
        super.afficherDetails();
        System.out.println("Sujet: " + sujet + ", Date publication: " + datePublication);
    }
}