USE gestion_stocks_ventes;

-- Insertion de produits d’exemple
INSERT INTO produit (nom, categorie, prix_unitaire, quantite_stock, seuil_reapprovisionnement) VALUES
('Stylo Bic', 'Papeterie', 1.50, 100, 20),
('Carnet A4', 'Papeterie', 3.75, 50, 10),
('Classeur A4', 'Papeterie', 7.20, 30, 5),
('Cahier Maths', 'Livres', 5.00, 40, 10),
('Livre Java 101', 'Livres', 25.00, 15, 5),
('Souris USB', 'Informatique', 12.50, 25, 5),
('Clé USB 16Go', 'Informatique', 8.00, 60, 10),
('Chargeur Universel', 'Électronique', 15.00, 10, 5),
('Toner LaserJet', 'Informatique', 45.00, 8, 3),
('Papier A4 500 feuilles', 'Papeterie', 6.00, 20, 5);

-- Insertion de quelques commandes + lignes de commande
INSERT INTO commande (nom_client, date_commande) VALUES
('Client A', '2025-05-20'),
('Client B', '2025-05-21'),
('Client C', '2025-05-22');

INSERT INTO ligne_commande (commande_id, produit_id, quantite, prix_unitaire) VALUES
(1, 1, 10, 1.50),
(1, 2, 5, 3.75),
(2, 6, 2, 12.50),
(2, 7, 4, 8.00),
(3, 3, 2, 7.20),
(3, 4, 1, 5.00);
