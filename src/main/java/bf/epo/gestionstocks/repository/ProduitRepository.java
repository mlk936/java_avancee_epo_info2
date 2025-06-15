package bf.epo.gestionstocks.repository;

import bf.epo.gestionstocks.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query("SELECT p FROM Produit p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :motCle, '%')) OR LOWER(p.categorie) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    List<Produit> rechercherMultiChamps(@Param("motCle") String motCle);


    // Recherche  catégorie
    List<Produit> findByCategorie(String categorie);

    // Liste ruptur stock
    List<Produit> findByQuantiteStockEquals(Integer quantite);



}
