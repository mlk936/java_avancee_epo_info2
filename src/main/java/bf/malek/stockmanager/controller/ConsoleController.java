package bf.malek.stockmanager.controller;

import bf.malek.stockmanager.entity.Produit;
import bf.malek.stockmanager.service.ProduitService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleController {

    @Autowired
    private ProduitService produitService;

    @PostConstruct
    public void lancerInterface() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== GESTION DE STOCK ===");

        while (true) {
            System.out.println("\n1. Ajouter produit");
            System.out.println("2. Liste produits");
            System.out.println("3. Produits en alerte stock");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");

            int choix;
            try {
                choix = scanner.nextInt();
                scanner.nextLine(); // consommer le reste de la ligne
            } catch (InputMismatchException e) {
                System.out.println("Entrée invalide, veuillez saisir un nombre.");
                scanner.nextLine(); // consommer la mauvaise entrée
                continue;
            }

            switch (choix) {
                case 1 -> {
                    System.out.print("Nom : ");
                    String nom = scanner.nextLine();
                    System.out.print("Catégorie : ");
                    String cat = scanner.nextLine();

                    double prix;
                    while (true) {
                        System.out.print("Prix unitaire : ");
                        try {
                            prix = scanner.nextDouble();
                            scanner.nextLine();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Veuillez saisir un nombre valide pour le prix.");
                            scanner.nextLine();
                        }
                    }

                    int stock;
                    while (true) {
                        System.out.print("Quantité en stock : ");
                        try {
                            stock = scanner.nextInt();
                            scanner.nextLine();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Veuillez saisir un entier valide pour la quantité.");
                            scanner.nextLine();
                        }
                    }

                    produitService.ajouterProduit(new Produit(nom, cat, prix, stock));
                    System.out.println("Produit ajouté !");
                }
                case 2 -> {
                    List<Produit> produits = produitService.afficherTousLesProduits();
                    if (produits.isEmpty()) {
                        System.out.println("Aucun produit disponible.");
                    } else {
                        produits.forEach(p -> System.out.println(p.getId() + ": " + p.getNom() + " - " + p.getQuantiteEnStock()));
                    }
                }
                case 3 -> {
                    int seuil;
                    while (true) {
                        System.out.print("Seuil : ");
                        try {
                            seuil = scanner.nextInt();
                            scanner.nextLine();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Veuillez saisir un entier valide pour le seuil.");
                            scanner.nextLine();
                        }
                    }
                    List<Produit> alertes = produitService.produitsAlerteStock(seuil);
                    if (alertes.isEmpty()) {
                        System.out.println("Aucun produit en alerte stock.");
                    } else {
                        alertes.forEach(p -> System.out.println(p.getNom() + " : " + p.getQuantiteEnStock()));
                    }
                }
                case 0 -> {
                    System.out.println("Fermeture...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Choix invalide !");
            }
        }
    }
}
