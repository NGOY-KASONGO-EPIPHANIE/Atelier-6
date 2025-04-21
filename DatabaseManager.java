package org.example.db;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseManager {
    private Connection connection;
    private String url;
    private String user;
    private String password;

    public DatabaseManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
            properties.setProperty("useSSL", "false");
            properties.setProperty("autoReconnect", "true");

            this.connection = DriverManager.getConnection(url, properties);
            initialiserBase();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erreur de connexion à la base: " + e.getMessage());
        }
    }

    private void initialiserBase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Création des tables
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS utilisateurs (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    mot_de_passe VARCHAR(100) NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS bibliothecaires (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    id_bibliothecaire VARCHAR(50) UNIQUE NOT NULL,
                    matricule VARCHAR(50) UNIQUE NOT NULL,
                    mot_de_passe VARCHAR(100) NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS livres (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    titre VARCHAR(100) NOT NULL,
                    auteur VARCHAR(100) NOT NULL,
                    nbr_pages INT NOT NULL,
                    type ENUM('ROMAN','BIOGRAPHIE') NOT NULL,
                    genre VARCHAR(100),
                    theme VARCHAR(100),
                    sujet VARCHAR(100),
                    date_publication DATE
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS emprunts (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    id_utilisateur INT NOT NULL,
                    id_livre INT NOT NULL,
                    date_emprunt DATE NOT NULL,
                    date_retour DATE,
                    FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id),
                    FOREIGN KEY (id_livre) REFERENCES livres(id)
                )
            """);

            // Vérifier si des données de test sont nécessaires
            verifierDonneesInitiales();
        }
    }

    private void verifierDonneesInitiales() throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM bibliothecaires")) {
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.executeUpdate("""
                    INSERT INTO bibliothecaires (nom, id_bibliothecaire, matricule, mot_de_passe)
                    VALUES ('Admin', 'BIB123', '22NK399', 'admin123')
                """);
            }
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur fermeture connexion: " + e.getMessage());
        }
    }
}