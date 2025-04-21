package org.example;

import org.example.db.DatabaseManager;
import org.example.service.*;
import org.example.ui.*;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;

public class BibliothequeApp {
    public static void main(String[] args) {
        // Chargement de la configuration
        Properties config = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            config.load(input);
        } catch (Exception e) {
            System.err.println("Erreur de chargement de la configuration: " + e.getMessage());
            return;
        }

        // Initialisation de la base de données
        DatabaseManager dbManager = new DatabaseManager(
                config.getProperty("db.url"),
                config.getProperty("db.user"),
                config.getProperty("db.password")
        );

        // Initialisation des services
        BibliothequeService bibliothequeService = new BibliothequeService(dbManager);
        AuthentificationService authService = new AuthentificationService(dbManager);

        // Initialisation des interfaces
        MenuBibliothecaire menuBib = new MenuBibliothecaire(bibliothequeService);
        MenuUtilisateur menuUser = null;

        try {
            // Menu principal
            int choix;
            do {
                ConsoleUI.clearConsole();
                ConsoleUI.afficherMessage("\n=== MENU PRINCIPAL ===");
                ConsoleUI.afficherMessage("1. Espace Bibliothécaire");
                ConsoleUI.afficherMessage("2. Espace Utilisateur");
                ConsoleUI.afficherMessage("3. Quitter");

                choix = ConsoleUI.demanderInt("Choix : ");

                switch (choix) {
                    case 1 -> espaceBibliothecaire(authService, menuBib);
                    case 2 -> espaceUtilisateur(bibliothequeService, menuUser);
                }
            } while (choix != 3);

        } catch (Exception e) {
            ConsoleUI.afficherErreur("Erreur: " + e.getMessage());
        } finally {
            dbManager.close();
        }
    }

    private static void espaceBibliothecaire(AuthentificationService authService, MenuBibliothecaire menuBib)
            throws SQLException {
        ConsoleUI.clearConsole();
        String id = ConsoleUI.demanderString("ID Bibliothécaire : ");
        String mdp = ConsoleUI.demanderString("Mot de passe : ");

        Bibliothecaire bib = authService.connecterBibliothecaire(id, mdp);
        if (bib != null) {
            menuBib.gererLivres();
        } else {
            ConsoleUI.afficherErreur("Identifiants incorrects !");
        }
    }

    private static void espaceUtilisateur(BibliothequeService service, MenuUtilisateur menuUser)
            throws SQLException {
        ConsoleUI.clearConsole();
        ConsoleUI.afficherMessage("1. S'inscrire");
        ConsoleUI.afficherMessage("2. Se connecter");

        int choix = ConsoleUI.demanderInt("Choix : ");

        if (choix == 1) {
            inscrireUtilisateur(service);
        } else if (choix == 2) {
            connecterUtilisateur(service, menuUser);
        }
    }

    // ... autres méthodes
}