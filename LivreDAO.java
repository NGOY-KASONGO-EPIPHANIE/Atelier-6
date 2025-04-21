package org.example.db;

import org.example.Model.Biographie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {
    private final DatabaseManager dbManager;

    public LivreDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void sauvegarder(Livre livre) throws SQLException {
        String sql = """
            INSERT INTO livres (titre, auteur, nbr_pages, type, genre, theme, sujet, date_publication)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparerStatementPourLivre(pstmt, livre);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    livre.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Livre> chargerTous() throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(creerLivreDepuisResultSet(rs));
            }
        }
        return livres;
    }

    public Livre trouverParTitre(String titre) throws SQLException {
        String sql = "SELECT * FROM livres WHERE titre = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, titre);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return creerLivreDepuisResultSet(rs);
                }
            }
        }
        return null;
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM livres WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public void mettreAJour(Livre livre) throws SQLException {
        String sql = """
            UPDATE livres 
            SET titre = ?, auteur = ?, nbr_pages = ?, type = ?, 
                genre = ?, theme = ?, sujet = ?, date_publication = ?
            WHERE id = ?
            """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            preparerStatementPourLivre(pstmt, livre);
            pstmt.setInt(9, livre.getId());
            pstmt.executeUpdate();
        }
    }

    public List<Livre> rechercherParTitreOuAuteur(String terme) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE titre LIKE ? OR auteur LIKE ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + terme + "%");
            pstmt.setString(2, "%" + terme + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    livres.add(creerLivreDepuisResultSet(rs));
                }
            }
        }
        return livres;
    }

    private void preparerStatementPourLivre(PreparedStatement pstmt, Livre livre) throws SQLException {
        pstmt.setString(1, livre.getTitre());
        pstmt.setString(2, livre.getAuteur());
        pstmt.setInt(3, livre.getNbrPages());

        if (livre instanceof Roman) {
            Roman roman = (Roman) livre;
            pstmt.setString(4, "ROMAN");
            pstmt.setString(5, roman.getGenre());
            pstmt.setString(6, roman.getTheme());
            pstmt.setNull(7, Types.VARCHAR);
            pstmt.setNull(8, Types.DATE);
        } else if (livre instanceof Biographie) {
            Biographie bio = (Biographie) livre;
            pstmt.setString(4, "BIOGRAPHIE");
            pstmt.setNull(5, Types.VARCHAR);
            pstmt.setNull(6, Types.VARCHAR);
            pstmt.setString(7, bio.getSujet());
            pstmt.setDate(8, Date.valueOf(bio.getDatePublication()));
        }
    }

    private Livre creerLivreDepuisResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String titre = rs.getString("titre");
        String auteur = rs.getString("auteur");
        int nbrPages = rs.getInt("nbr_pages");
        String type = rs.getString("type");

        if ("ROMAN".equals(type)) {
            return new Roman(
                    id, titre, auteur, nbrPages,
                    rs.getString("genre"),
                    rs.getString("theme")
            );
        } else {
            Date datePub = rs.getDate("date_publication");
            return new Biographie(
                    id, titre, auteur, nbrPages,
                    rs.getString("sujet"),
                    datePub != null ? datePub.toLocalDate() : null
            );
        }
    }
}