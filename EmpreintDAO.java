package org.example.db;

import org.example.model.Emprunt;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {
    private final DatabaseManager dbManager;

    public EmpruntDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void enregistrerEmprunt(int idUtilisateur, int idLivre) throws SQLException {
        String sql = """
            INSERT INTO emprunts (id_utilisateur, id_livre, date_emprunt)
            VALUES (?, ?, ?)
            """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUtilisateur);
            pstmt.setInt(2, idLivre);
            pstmt.setDate(3, Date.valueOf(LocalDate.now()));
            pstmt.executeUpdate();
        }
    }

    public void enregistrerRetour(int idEmprunt) throws SQLException {
        String sql = "UPDATE emprunts SET date_retour = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.setInt(2, idEmprunt);
            pstmt.executeUpdate();
        }
    }

    public List<Emprunt> listerEmpruntsEnCours() throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = """
            SELECT e.*, u.nom AS nom_utilisateur, l.titre AS titre_livre
            FROM emprunts e
            JOIN utilisateurs u ON e.id_utilisateur = u.id
            JOIN livres l ON e.id_livre = l.id
            WHERE e.date_retour IS NULL
            """;

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                emprunts.add(new Emprunt(
                        rs.getInt("id"),
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom_utilisateur"),
                        rs.getInt("id_livre"),
                        rs.getString("titre_livre"),
                        rs.getDate("date_emprunt").toLocalDate(),
                        rs.getDate("date_retour") != null ? rs.getDate("date_retour").toLocalDate() : null
                ));
            }
        }
        return emprunts;
    }

    public List<Emprunt> listerHistoriqueParUtilisateur(int idUtilisateur) throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = """
            SELECT e.*, l.titre AS titre_livre
            FROM emprunts e
            JOIN livres l ON e.id_livre = l.id
            WHERE e.id_utilisateur = ?
            ORDER BY e.date_emprunt DESC
            """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUtilisateur);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    emprunts.add(new Emprunt(
                            rs.getInt("id"),
                            idUtilisateur,
                            null,
                            rs.getInt("id_livre"),
                            rs.getString("titre_livre"),
                            rs.getDate("date_emprunt").toLocalDate(),
                            rs.getDate("date_retour") != null ? rs.getDate("date_retour").toLocalDate() : null
                    ));
                }
            }
        }
        return emprunts;
    }

    public Emprunt trouverEmpruntEnCours(int idLivre) throws SQLException {
        String sql = """
            SELECT e.*, u.nom AS nom_utilisateur
            FROM emprunts e
            JOIN utilisateurs u ON e.id_utilisateur = u.id
            WHERE e.id_livre = ? AND e.date_retour IS NULL
            """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idLivre);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Emprunt(
                            rs.getInt("id"),
                            rs.getInt("id_utilisateur"),
                            rs.getString("nom_utilisateur"),
                            idLivre,
                            null,
                            rs.getDate("date_emprunt").toLocalDate(),
                            null
                    );
                }
            }
        }
        return null;
    }
}