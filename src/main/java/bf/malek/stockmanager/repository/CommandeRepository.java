package bf.malek.stockmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bf.malek.stockmanager.entity.Commande;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    
}
