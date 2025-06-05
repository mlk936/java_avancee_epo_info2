package bf.epo.gestionstocks.repository;


import bf.epo.gestionstocks.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    // Récupérer toutes les commandes par date
    List<Commande> findByDateCommande(LocalDate dateCommande);
}


