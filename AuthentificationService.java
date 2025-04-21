package org.example.service;

import org.example.Bibliothecaire;
import org.example.db.BibliographerDAO;
import org.example.db.DatabaseManager;
import java.sql.SQLException;

public class AuthentificationService {
    private final BibliographerDAO bibliothecaireDAO;

    public AuthentificationService(DatabaseManager dbManager) {
        this.bibliothecaireDAO = new BibliographerDAO(dbManager);
    }

    public Bibliothecaire connecterBibliothecaire(String id, String mdp) throws SQLException {
        Bibliothecaire bib = bibliothecaireDAO.trouverParIdentifiant(id);
        if (bib != null && bib.getMotDePasse().equals(mdp)) {
            return bib;
        }
        return null;
    }

    public void ajouterBibliothecaire(Bibliothecaire bibliothecaire) throws SQLException {
        bibliothecaireDAO.sauvegarder(bibliothecaire);
    }

    public List<Bibliothecaire> listerBibliothecaires() throws SQLException {
        return bibliothecaireDAO.chargerTous();
    }

    public void modifierBibliothecaire(Bibliothecaire bibliothecaire) throws SQLException {
        bibliothecaireDAO.mettreAJour(bibliothecaire);
    }

    public void supprimerBibliothecaire(String idBibliothecaire) throws SQLException {
        bibliothecaireDAO.supprimer(idBibliothecaire);
    }
}