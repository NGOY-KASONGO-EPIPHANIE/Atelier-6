package org.example;

import java.time.LocalDate;

public interface BiographieInterface {
    // Getters et setters
    String getSujet();

    void setSujet(String sujet);

    LocalDate getDatePublication();

    void setDatePublication(LocalDate datePublication);

    void afficherDetails();
}
