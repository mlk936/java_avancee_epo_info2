-- ====================================================
-- Création de la base et des tables (MySQL)
-- ====================================================

-- 1. Création de la base (si nécessaire) :
CREATE DATABASE IF NOT EXISTS gestion_stocks_ventes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gestion_stocks_ventes;

-- 2. Table produit
CREATE TABLE IF NOT EXISTS produit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    categorie VARCHAR(50) NOT NULL,
    prix_unitaire DECIMAL(10,2) NOT NULL,
    quantite_stock INT NOT NULL,
    seuil_reapprovisionnement INT NOT NULL DEFAULT 10
);

-- 3. Table commande
CREATE TABLE IF NOT EXISTS commande (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom_client VARCHAR(100) NOT NULL,
    date_commande DATE NOT NULL
);

-- 4. Table ligne_commande
CREATE TABLE IF NOT EXISTS ligne_commande (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    commande_id BIGINT NOT NULL,
    produit_id BIGINT NOT NULL,
    quantite INT NOT NULL,
    prix_unitaire DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (commande_id) REFERENCES commande(id) ON DELETE CASCADE,
    FOREIGN KEY (produit_id) REFERENCES produit(id)
);

-- 5. Index
CREATE INDEX idx_produit_categorie ON produit(categorie);
CREATE INDEX idx_ligne_commande_commande ON ligne_commande(commande_id);
