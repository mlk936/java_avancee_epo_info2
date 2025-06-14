package bf.epo.gestionstocks.controller;

import bf.epo.gestionstocks.model.Produit;
import bf.epo.gestionstocks.service.ProduitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    // Lister tous les produits
    @GetMapping
    public List<Produit> getAllProduits() {
        return produitService.listerProduits();
    }

    // Obtenir un produit par ID
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        Produit produit = produitService.getProduitParId(id);
        return ResponseEntity.ok(produit);
    }

    // Ajouter un nouveau produit avec image optionnelle
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Produit> creerProduit(
            @RequestParam String nom,
            @RequestParam String categorie,
            @RequestParam double prixUnitaire,
            @RequestParam int quantiteStock,
            @RequestParam int seuilReapprovisionnement,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile
    ) {
        Produit produit = new Produit();
        produit.setNom(nom);
        produit.setCategorie(categorie);
        produit.setPrixUnitaire(prixUnitaire);
        produit.setQuantiteStock(quantiteStock);
        produit.setSeuilReapprovisionnement(seuilReapprovisionnement);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = produitService.enregistrerImage(imageFile);
            produit.setImageUrl(imageUrl);
        } else {
            produit.setImageUrl("/images/default.png"); // <-- ajout de l'image par défaut
        }

        Produit saved = produitService.sauvegarderProduit(produit);
        return ResponseEntity.ok(saved);
    }


    // Modifier un produit existant avec image optionnelle
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Produit> updateProduitAvecImage(
            @PathVariable Long id,
            @RequestParam String nom,
            @RequestParam String categorie,
            @RequestParam double prixUnitaire,
            @RequestParam int quantiteStock,
            @RequestParam int seuilReapprovisionnement,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile
    ) {
        Produit updated = produitService.updateProduitAvecImage(
                id, nom, categorie, prixUnitaire,
                quantiteStock, seuilReapprovisionnement, imageFile
        );
        return ResponseEntity.ok(updated);
    }

    // Supprimer un produit par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerProduit(@PathVariable Long id) {
        produitService.supprimerProduit(id);
        return ResponseEntity.noContent().build();
    }
}
