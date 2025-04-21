# Système de Gestion de Bibliothèque

## Description
Application Java de gestion de bibliothèque avec persistance MySQL.
1. Créer la base de données:
```sql
CREATE DATABASE bibliotheque_db;
CREATE USER 'bibliotheque_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON bibliotheque_db.* TO 'bibliothecaire_user'@'localhost';
FLUSH PRIVILEGES;
