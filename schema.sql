-- Supprime
DROP TABLE IF EXISTS ligne_commande;
DROP TABLE IF EXISTS commande;
DROP TABLE IF EXISTS produit;

-- Table produit
CREATE TABLE produit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    categorie VARCHAR(50) NOT NULL,
    prix_unitaire DOUBLE NOT NULL CHECK (prix_unitaire >= 0),
    quantite_stock INT NOT NULL CHECK (quantite_stock >= 0),
    seuil_reapprovisionnement INT NOT NULL CHECK (seuil_reapprovisionnement >= 0),
    ventes INT DEFAULT 0,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    image_url VARCHAR(255) DEFAULT '/images/produits/default.png'
);

-- Table commande
CREATE TABLE commande (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom_client VARCHAR(100) NOT NULL,
    date_commande DATE NOT NULL
);

-- Table ligne_commande
CREATE TABLE ligne_commande (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    commande_id BIGINT NOT NULL,
    produit_id BIGINT NOT NULL,
    quantite INT NOT NULL CHECK (quantite >= 1),
    prix_unitaire DOUBLE NOT NULL CHECK (prix_unitaire >= 0),
    FOREIGN KEY (commande_id) REFERENCES commande(id) ON DELETE CASCADE,
    FOREIGN KEY (produit_id) REFERENCES produit(id)
);
