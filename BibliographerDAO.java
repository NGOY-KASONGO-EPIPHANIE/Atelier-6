package org.example.db;

import org.example.Bibliothecaire;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BibliographerDAO {
    private final DatabaseManager dbManager;

    public BibliographerDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public Bibliothecaire trouverParIdentifiant(String idBibliothecaire) throws SQLException {
        String sql = "SELECT * FROM bibliothecaires WHERE id_bibliothecaire = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idBibliothecaire);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Bibliothecaire(
                            rs.getString("nom"),
                            rs.getString("id_bibliothecaire"),
                            rs.getString("matricule"),
                            rs.getString("mot_de_passe")
                    );
                }
            }
        }
        return null;
    }

    public void sauvegarder(Bibliothecaire bibliothecaire) throws SQLException {
        String sql = """
            INSERT INTO bibliothecaires (nom, id_bibliothecaire, matricule, mot_de_passe)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bibliothecaire.getNom());
            pstmt.setString(2, bibliothecaire.getIdBibliothecaire());
            pstmt.setString(3, bibliothecaire.getMatricule());
            pstmt.setString(4, bibliothecaire.getMotDePasse());
            pstmt.executeUpdate();
        }
    }

    public List<Bibliothecaire> chargerTous() throws SQLException {
        List<Bibliothecaire> bibliothecaires = new ArrayList<>();
        String sql = "SELECT * FROM bibliothecaires";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bibliothecaires.add(new Bibliothecaire(
                        rs.getString("nom"),
                        rs.getString("id_bibliothecaire"),
                        rs.getString("matricule"),
                        rs.getString("mot_de_passe")
                ));
            }
        }
        return bibliothecaires;
    }

    public void supprimer(String idBibliothecaire) throws SQLException {
        String sql = "DELETE FROM bibliothecaires WHERE id_bibliothecaire = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idBibliothecaire);
            pstmt.executeUpdate();
        }
    }

    public void mettreAJour(Bibliothecaire bibliothecaire) throws SQLException {
        String sql = """
            UPDATE bibliothecaires 
            SET nom = ?, matricule = ?, mot_de_passe = ?
            WHERE id_bibliothecaire = ?
            """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bibliothecaire.getNom());
            pstmt.setString(2, bibliothecaire.getMatricule());
            pstmt.setString(3, bibliothecaire.getMotDePasse());
            pstmt.setString(4, bibliothecaire.getIdBibliothecaire());
            pstmt.executeUpdate();
        }
    }
}