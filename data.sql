-- Produits
INSERT INTO produit (nom, categorie, prix_unitaire, quantite_stock, seuil_reapprovisionnement, ventes, date_ajout, image_url)
VALUES
  ('Ordinateur Portable', 'Informatique', 450000, 15, 5, 3, NOW(), '/images/produits/laptop.png'),
  ('Souris Optique', 'Accessoires', 5000, 40, 10, 10, NOW(), '/images/produits/souris.png'),
  ('Imprimante Laser', 'Bureau', 120000, 8, 3, 2, NOW(), '/images/produits/imprimante.png');

-- Commandes
INSERT INTO commande (nom_client, date_commande)
VALUES
  ('Alice KABORE', '2025-06-10'),
  ('Jean OUEDRAOGO', '2025-06-12');

-- Lignes de commande
INSERT INTO ligne_commande (commande_id, produit_id, quantite, prix_unitaire)
VALUES
  (1, 1, 1, 450000),
  (1, 2, 2, 5000),
  (2, 3, 1, 120000);
