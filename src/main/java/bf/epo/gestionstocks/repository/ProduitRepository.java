package bf.epo.gestionstocks.repository;

import bf.epo.gestionstocks.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // Liste les produits dont le stock est inférieur ou égal au seuil
    List<Produit> findByQuantiteStockLessThanEqual(Integer seuilReapprovisionnement);

    // Top 4 produits les plus vendus (suppose champ 'ventes' dans Produit)
    List<Produit> findTop4ByOrderByVentesDesc();

    // Top 4 produits les plus récemment ajoutés (suppose champ 'dateAjout' dans Produit)
    List<Produit> findTop4ByOrderByDateAjoutDesc();

    // Recherche les produits par nom contenant une chaîne (insensible à la casse)
    List<Produit> findByNomContainingIgnoreCase(String nom);

    // Recherche les produits par catégorie exacte
    List<Produit> findByCategorie(String categorie);

    // Liste les produits avec stock exactement égal à 0 (rupture de stock)
    List<Produit> findByQuantiteStockEquals(Integer quantite);



}
