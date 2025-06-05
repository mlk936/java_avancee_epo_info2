package bf.epo.gestionstocks.controller;

import bf.epo.gestionstocks.model.Produit;
import bf.epo.gestionstocks.service.ProduitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    // 1. Lister tous les produits
    @GetMapping
    public ResponseEntity<List<Produit>> getAllProduits() {
        List<Produit> produits = produitService.listerProduits();
        return ResponseEntity.ok(produits);
    }

    // 2. Récupérer un produit par ID
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        Produit p = produitService.getProduitParId(id);
        if (p == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produit non trouvé");
        }
        return ResponseEntity.ok(p);
    }

    // 3. Ajouter un produit
    @PostMapping
    public ResponseEntity<Produit> createProduit(@Valid @RequestBody Produit produit) {
        Produit saved = produitService.sauvegarderProduit(produit);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // 4. Modifier un produit
    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(
            @PathVariable Long id,
            @Valid @RequestBody Produit produitRequest) {
        Produit existing = produitService.getProduitParId(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produit non trouvé");
        }
        existing.setNom(produitRequest.getNom());
        existing.setCategorie(produitRequest.getCategorie());
        existing.setPrixUnitaire(produitRequest.getPrixUnitaire());
        existing.setQuantiteStock(produitRequest.getQuantiteStock());
        existing.setSeuilReapprovisionnement(produitRequest.getSeuilReapprovisionnement());
        Produit updated = produitService.sauvegarderProduit(existing);
        return ResponseEntity.ok(updated);
    }

    // 5. Supprimer un produit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        Produit existing = produitService.getProduitParId(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produit non trouvé");
        }
        produitService.supprimerProduit(id);
        return ResponseEntity.noContent().build();
    }
}
