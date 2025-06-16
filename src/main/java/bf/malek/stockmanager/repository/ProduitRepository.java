package bf.malek.stockmanager.repository;

import bf.malek.stockmanager.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    
    // Retourne les produits dont la quantité en stock est inférieure ou égale à un certain seuil
    List<Produit> findByQuantiteEnStockLessThanEqual(int seuil);
}


