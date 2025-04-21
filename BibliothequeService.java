package org.example.service;

import org.example.model.*;
import org.example.db.*;
import java.sql.SQLException;
import java.util.List;

public class BibliothequeService {
    private final LivreDAO livreDAO;
    private final UtilisateurDAO utilisateurDAO;
    private final EmpruntDAO empruntDAO;

    public BibliothequeService(DatabaseManager dbManager) {
        this.livreDAO = new LivreDAO(dbManager);
        this.utilisateurDAO = new UtilisateurDAO(dbManager);
        this.empruntDAO = new EmpruntDAO(dbManager);
    }

    // Gestion des livres
    public List<Livre> listerLivres() throws SQLException {
        return livreDAO.chargerTous();
    }

    public Livre trouverLivreParTitre(String titre) throws SQLException {
        return livreDAO.trouverParTitre(titre);
    }

    public void ajouterLivre(Livre livre) throws SQLException {
        livreDAO.sauvegarder(livre);
    }

    public void modifierLivre(Livre livre) throws SQLException {
        livreDAO.mettreAJour(livre);
    }

    public void supprimerLivre(int id) throws SQLException {
        livreDAO.supprimer(id);
    }

    public List<Livre> rechercherLivres(String terme) throws SQLException {
        return livreDAO.rechercherParTitreOuAuteur(terme);
    }

    // Gestion des utilisateurs
    public void inscrireUtilisateur(Utilisateur utilisateur) throws SQLException {
        utilisateurDAO.sauvegarder(utilisateur);
    }

    public Utilisateur connecterUtilisateur(String email, String mdp) throws SQLException {
        Utilisateur user = utilisateurDAO.trouverParEmail(email);
        if (user != null && user.verifierMotDePasse(mdp)) {
            return user;
        }
        return null;
    }

    public List<Utilisateur> listerUtilisateurs() throws SQLException {
        return utilisateurDAO.chargerTous();
    }

    public void modifierUtilisateur(Utilisateur utilisateur) throws SQLException {
        utilisateurDAO.mettreAJour(utilisateur);
    }

    public void supprimerUtilisateur(int id) throws SQLException {
        utilisateurDAO.supprimer(id);
    }

    // Gestion des emprunts
    public void emprunterLivre(int idUtilisateur, int idLivre) throws SQLException {
        empruntDAO.enregistrerEmprunt(idUtilisateur, idLivre);
    }

    public void retournerLivre(int idEmprunt) throws SQLException {
        empruntDAO.enregistrerRetour(idEmprunt);
    }

    public List<Emprunt> listerEmpruntsEnCours() throws SQLException {
        return empruntDAO.listerEmpruntsEnCours();
    }

    public List<Emprunt> listerHistoriqueUtilisateur(int idUtilisateur) throws SQLException {
        return empruntDAO.listerHistoriqueParUtilisateur(idUtilisateur);
    }
}