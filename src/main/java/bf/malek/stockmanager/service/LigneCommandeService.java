package bf.malek.stockmanager.service;

import bf.malek.stockmanager.entity.LigneCommande;
import bf.malek.stockmanager.repository.LigneCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LigneCommandeService {

    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    //Créer une ligne de commande
    public LigneCommande creer(LigneCommande ligneCommande) {
        return ligneCommandeRepository.save(ligneCommande);
    }

    //Lire toutes les lignes de commande
    public List<LigneCommande> lire() {
        return ligneCommandeRepository.findAll();
    }

    //Modifier une ligne de commande
    public LigneCommande modifier(Long id, LigneCommande nouvelleLigne) {
        return ligneCommandeRepository.findById(id).map(ligne -> {
            ligne.setProduit(nouvelleLigne.getProduit());
            ligne.setQuantite(nouvelleLigne.getQuantite());
            ligne.setCommande(nouvelleLigne.getCommande());
            return ligneCommandeRepository.save(ligne);
        }).orElse(null);
    }

    //Supprimer une ligne de commande
    public String supprimer(Long id) {
        ligneCommandeRepository.deleteById(id);
        return "Ligne de commande supprimée avec succès.";
    }
}
