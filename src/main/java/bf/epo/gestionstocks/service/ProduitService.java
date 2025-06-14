package bf.epo.gestionstocks.service;

import bf.epo.gestionstocks.exception.ResourceNotFoundException;
import bf.epo.gestionstocks.model.Produit;
import bf.epo.gestionstocks.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    // Produits vedettes (top 4 ventes)
    public List<Produit> getProduitsVedettes() {
        return produitRepository.findTop4ByOrderByVentesDesc();
    }

    // Nouveaux produits (top 4 date ajout)
    public List<Produit> getNouveauxProduits() {
        return produitRepository.findTop4ByOrderByDateAjoutDesc();
    }

    // Ajouter ou modifier un produit
    public Produit sauvegarderProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    // Récupérer un produit par ID
    public Produit getProduitParId(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable avec l'ID : " + id));
    }

    // Supprimer un produit par ID
    public void supprimerProduit(Long id) {
        Produit produit = getProduitParId(id);
        produitRepository.delete(produit);
    }

    // Lister les produits à réapprovisionner
    public List<Produit> listerProduitsARacheter(Integer seuil) {
        return produitRepository.findByQuantiteStockLessThanEqual(seuil);
    }

    public List<Produit> listerProduitsARacheter() {
        return listerProduitsARacheter(10);
    }

    // Produits en stock critique
    public List<Produit> getProduitsStockCritique() {
        return produitRepository.findAll()
                .stream()
                .filter(p -> p.getQuantiteStock() <= p.getSeuilReapprovisionnement())
                .collect(Collectors.toList());
    }

    // Enregistrer une image et retourner l'URL
    public String enregistrerImage(MultipartFile image) {
        try {
            String dossier = "uploads/";
            String nomFichier = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path chemin = Paths.get(dossier + nomFichier);
            Files.createDirectories(chemin.getParent());
            Files.write(chemin, image.getBytes());
            return "/images/" + nomFichier;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l’enregistrement de l’image", e);
        }
    }

    // Mise à jour avec image
    public Produit updateProduitAvecImage(Long id, String nom, String categorie, double prixUnitaire,
                                          int quantiteStock, int seuilReapprovisionnement, MultipartFile image) {
        Produit existing = getProduitParId(id);
        existing.setNom(nom);
        existing.setCategorie(categorie);
        existing.setPrixUnitaire(prixUnitaire);
        existing.setQuantiteStock(quantiteStock);
        existing.setSeuilReapprovisionnement(seuilReapprovisionnement);

        if (image != null && !image.isEmpty()) {
            String imageUrl = enregistrerImage(image);
            existing.setImageUrl(imageUrl);
        }
        return sauvegarderProduit(existing);
    }
}
