# Gestion Stocks & Ventes (Spring Boot)

## 1. Présentation

Application REST Spring Boot permettant de :
- Gérer le catalogue de produits (CRUD complet)
- Gérer des commandes clients (vérification de stock, mise à jour du stock)
- Générer des factures au format texte
- Produire un rapport quotidien (nombre commandes, chiffre d’affaires, top 5 produits)

## 2. Prérequis

- Java 11 (ou supérieur)
- Maven 3.6+
- MySQL (ou un autre SGBD compatible, en adaptant `schema.sql`)
- (Optionnel) Git

## 3. Installation de la base de données

1. Créez la base de données MySQL :
   ```sql
   CREATE DATABASE IF NOT EXISTS gestion_stocks_ventes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
