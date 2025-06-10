package bf.epo.gestionstocks.repository;

import bf.epo.gestionstocks.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    // Méthode dérivée pour lister les produits dont le stock ≤ seuil
    List<Produit> findByQuantiteStockLessThanEqual(Integer seuilReapprovisionnement);

    List<Produit> findTop4ByOrderByVentesDesc();
    List<Produit> findTop4ByOrderByDateAjoutDesc();

}
