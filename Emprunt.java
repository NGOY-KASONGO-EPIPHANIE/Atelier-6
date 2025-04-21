package org.example.Model;

import java.time.LocalDate;

public class Emprunt {
    private int id;
    private int idUtilisateur;
    private String nomUtilisateur;
    private int idLivre;
    private String titreLivre;
    private LocalDate dateEmprunt;
    private LocalDate dateRetour;

    public Emprunt(int id, int idUtilisateur, String nomUtilisateur, int idLivre,
                   String titreLivre, LocalDate dateEmprunt, LocalDate dateRetour) {
        this.id = id;
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.idLivre = idLivre;
        this.titreLivre = titreLivre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    public int getIdLivre() { return idLivre; }
    public void setIdLivre(int idLivre) { this.idLivre = idLivre; }
    public String getTitreLivre() { return titreLivre; }
    public void setTitreLivre(String titreLivre) { this.titreLivre = titreLivre; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public void setDateEmprunt(LocalDate dateEmprunt) { this.dateEmprunt = dateEmprunt; }
    public LocalDate getDateRetour() { return dateRetour; }
    public void setDateRetour(LocalDate dateRetour) { this.dateRetour = dateRetour; }

    public boolean estEnCours() {
        return dateRetour == null;
    }

    @Override
    public String toString() {
        return "Emprunt [Livre: " + titreLivre +
                ", Utilisateur: " + nomUtilisateur +
                ", Emprunté le: " + dateEmprunt +
                (dateRetour != null ? ", Retourné le: " + dateRetour : ", En cours") + "]";
    }
}