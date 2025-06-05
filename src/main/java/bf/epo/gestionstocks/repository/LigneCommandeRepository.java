package bf.epo.gestionstocks.repository;


import bf.epo.gestionstocks.model.LigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
    // Lister toutes les lignes d'une commande donnée
    List<LigneCommande> findByCommandeId(Long commandeId);
}
