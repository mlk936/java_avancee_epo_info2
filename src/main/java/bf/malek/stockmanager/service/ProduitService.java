package bf.malek.stockmanager.service;

import bf.malek.stockmanager.entity.Produit;
import bf.malek.stockmanager.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;
    
    // Ajouter un produit
    public void ajouterProduit(Produit produit) {
        creer(produit);
    }
    
    // Afficher tous les produits
    public List<Produit> afficherTousLesProduits() {
        return lire();
    }
    
    // Produits avec quantité <= seuil (utilise méthode repository dédiée)
    public List<Produit> produitsAlerteStock(int seuil) {
        return produitRepository.findByQuantiteEnStockLessThanEqual(seuil);
    }

    //Créer un produit
    public Produit creer(Produit produit) {
        return produitRepository.save(produit);
    }

    //Lire tous les produits
    public List<Produit> lire() {
        return produitRepository.findAll();
    }

    //Lire un seul produit par son ID
    public Optional<Produit> lireParId(Long id) {
        return produitRepository.findById(id);
    }

    //Modifier un produit
    public Produit modifier(Long id, Produit nouveauProduit) {
        return produitRepository.findById(id).map(produit -> {
            produit.setNom(nouveauProduit.getNom());
            produit.setCategorie(nouveauProduit.getCategorie());
            produit.setPrixUnitaire(nouveauProduit.getPrixUnitaire());
            produit.setQuantiteEnStock(nouveauProduit.getQuantiteEnStock());
            return produitRepository.save(produit);
        }).orElse(null);
    }

    //Supprimer un produit
    public String supprimer(Long id) {
        produitRepository.deleteById(id);
        return "Produit supprimé avec succès.";
    }
}
