package bf.epo.gestionstocks.service;
import bf.epo.gestionstocks.exception.ResourceNotFoundException;
import bf.epo.gestionstocks.model.Produit;
import bf.epo.gestionstocks.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    // Lister tous les produits
    public List<Produit> listerProduits() {
        return produitRepository.findAll();
    }

    // Ajouter (ou modifier) un produit
    public Produit sauvegarderProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    // Récupérer un produit par ID (ou lancer exception si non trouvé)
    public Produit getProduitParId(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable avec l'ID : " + id));
    }

    // Supprimer un produit par ID
    public void supprimerProduit(Long id) {
        Produit p = getProduitParId(id);
        produitRepository.delete(p);
    }

    // Lister les produits à réapprovisionner (stock ≤ seuil)
    public List<Produit> listerProduitsARacheter() {
        return produitRepository.findByQuantiteStockLessThanEqual(
                produitRepository.findById(1L) // pour illustrer, mais on peut hardcoder  seuil ou le récupérer depuis config
                        .map(Produit::getSeuilReapprovisionnement)
                        .orElse(10)
        );
    }
}
