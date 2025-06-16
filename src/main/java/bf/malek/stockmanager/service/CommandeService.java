package bf.malek.stockmanager.service;

import bf.malek.stockmanager.entity.Commande;
import bf.malek.stockmanager.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    // Créer une commande
    public Commande creer(Commande commande) {
        return commandeRepository.save(commande);
    }

    // Lire toutes les commandes
    public List<Commande> lire() {
        return commandeRepository.findAll();
    }

    // Modifier une commande
    public Commande modifier(Long id, Commande nouvelleCommande) {
        return commandeRepository.findById(id).map(commande -> {
            commande.setClient(nouvelleCommande.getClient());
            commande.setLignes(nouvelleCommande.getLignes());
            return commandeRepository.save(commande);
        }).orElse(null);
    }

    // Supprimer une commande
    public String supprimer(Long id) {
        commandeRepository.deleteById(id);
        return "Commande supprimée avec succès.";
    }
}
