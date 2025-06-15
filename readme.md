# Gestion Stocks & Ventes (Spring Boot)

## 1. Présentation

Avant tout voici le lien git du projet
https://github.com/mlk936/java_avancee_epo_info2.git

Application REST Spring Boot permettant de :
- Gérer le catalogue de produits (CRUD complet)
- Gérer des commandes clients (vérification de stock, mise à jour du stock)
- Générer des factures au format texte (PDF à venir)
- Produire un rapport quotidien (nombre de commandes, chiffre d’affaires, top 5 produits)

## 2. Prérequis

- Java 11 (ou supérieur)
- Maven 3.6+
- MySQL (ou autre SGBD compatible, en adaptant `schema.sql`)
- (Optionnel) Git

## 3. Installation de la base de données

1. Créez la base de données MySQL :
   ```sql
   CREATE DATABASE IF NOT EXISTS gestion_stocks_ventes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


- mvn clean package
  spring.datasource.url=jdbc:mysql://localhost:3306/gestion_stocks_ventes?useSSL=false&serverTimezone=UTC
  spring.datasource.username=utilisateur
  spring.datasource.password=motdepasse
  spring.jpa.hibernate.ddl-auto=none
  spring.jpa.show-sql=true


Pour lancer les tests avec Maven

mvn test

