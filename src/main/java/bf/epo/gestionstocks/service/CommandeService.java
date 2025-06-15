package bf.epo.gestionstocks.service;

import bf.epo.gestionstocks.dto.CommandeRequest;
import bf.epo.gestionstocks.dto.LigneCommandeRequest;
import bf.epo.gestionstocks.exception.ResourceNotFoundException;
import bf.epo.gestionstocks.exception.StockInsuffisantException;
import bf.epo.gestionstocks.model.Commande;
import bf.epo.gestionstocks.model.LigneCommande;
import bf.epo.gestionstocks.model.Produit;
import bf.epo.gestionstocks.repository.CommandeRepository;
import bf.epo.gestionstocks.repository.LigneCommandeRepository;
import bf.epo.gestionstocks.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ProduitRepository produitRepository;
    private final LigneCommandeRepository ligneCommandeRepository;

    public CommandeService(CommandeRepository commandeRepository,
                           ProduitRepository produitRepository,
                           LigneCommandeRepository ligneCommandeRepository) {
        this.commandeRepository = commandeRepository;
        this.produitRepository = produitRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
    }

    /**
     * Crée une commande à partir d’une CommandeRequest,
     * vérifie le stock et ajuste les quantités.
     */
    @Transactional
    public Commande creerCommande(CommandeRequest request) {
        Commande commande = new Commande();
        commande.setNomClient(request.getNomClient());
        commande.setDateCommande(LocalDate.now());

        // Vérification du stock
        for (LigneCommandeRequest lcReq : request.getLignes()) {
            Produit p = produitRepository.findById(lcReq.getProduitId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Produit introuvable pour l'ID : " + lcReq.getProduitId()));
            if (lcReq.getQuantite() > p.getQuantiteStock()) {
                throw new StockInsuffisantException(String.format(
                        "Stock insuffisant pour le produit %s (ID=%d). Stock actuel : %d, demandé : %d",
                        p.getNom(), p.getId(), p.getQuantiteStock(), lcReq.getQuantite()));
            }
        }

        Commande savedCommande = commandeRepository.save(commande);

        List<LigneCommande> lignesToSave = new ArrayList<>();
        for (LigneCommandeRequest lcReq : request.getLignes()) {
            Produit p = produitRepository.findById(lcReq.getProduitId()).get();
            LigneCommande lc = new LigneCommande();
            lc.setProduit(p);
            lc.setQuantite(lcReq.getQuantite());
            lc.setPrixUnitaire(p.getPrixUnitaire());
            lc.setCommande(savedCommande);
            lignesToSave.add(lc);

            p.setQuantiteStock(p.getQuantiteStock() - lcReq.getQuantite());
            produitRepository.save(p);
        }

        ligneCommandeRepository.saveAll(lignesToSave);
        savedCommande.setLignes(lignesToSave);
        return savedCommande;
    }

    /**
     * Génère une facture texte pour une commande.
     */
    public String genererFacture(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Commande introuvable avec l'ID : " + commandeId));

        List<LigneCommande> lignes = ligneCommandeRepository.findByCommandeId(commandeId);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Facture pour la commande n°%d%n", commande.getId()));
        sb.append(String.format("Client : %s%n", commande.getNomClient()));
        sb.append(String.format("Date : %s%n%n", commande.getDateCommande()));
        sb.append(String.format("%-5s %-20s %-10s %-10s %-10s%n",
                "LIGNE", "PRODUIT", "PU", "QTE", "SOUS-TOTAL"));
        sb.append("----------------------------------------------------------------\n");

        double total = 0.0;
        int index = 1;
        for (LigneCommande lc : lignes) {
            String nomProd = lc.getProduit().getNom();
            double pu = lc.getPrixUnitaire();
            int qte = lc.getQuantite();
            double sousTotal = pu * qte;
            total += sousTotal;
            sb.append(String.format("%-5d %-20s %-10.2f %-10d %-10.2f%n",
                    index++, nomProd, pu, qte, sousTotal));
        }
        sb.append("----------------------------------------------------------------\n");
        sb.append(String.format("Montant total : %.2f FCFA%n", total));

        return sb.toString();
    }



    public List<Commande> getCommandesParDate(LocalDate date) {
        return commandeRepository.findByDateCommande(date);
    }


    public Map<String, Object> genererRapportDuJour(LocalDate date) {
        List<Commande> commandesDuJour = commandeRepository.findByDateCommande(date);
        int nombreCommandes = commandesDuJour.size();
        double chiffreAffaires = 0.0;

        Map<Long, Integer> produitQuantites = new HashMap<>();

        for (Commande commande : commandesDuJour) {
            List<LigneCommande> lignes = ligneCommandeRepository.findByCommandeId(commande.getId());
            for (LigneCommande ligne : lignes) {
                Long produitId = ligne.getProduit().getId();
                int quantite = ligne.getQuantite();
                chiffreAffaires += quantite * ligne.getPrixUnitaire();
                produitQuantites.merge(produitId, quantite, Integer::sum);
            }
        }

        List<Map<String, Object>> topProduits = produitQuantites.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .map(entry -> {
                    Long produitId = entry.getKey();
                    int quantiteVendue = entry.getValue();
                    Produit produit = produitRepository.findById(produitId).orElse(null);
                    if (produit != null) {
                        Map<String, Object> produitInfo = new HashMap<>();
                        produitInfo.put("id", produit.getId());
                        produitInfo.put("nom", produit.getNom());
                        produitInfo.put("categorie", produit.getCategorie());
                        produitInfo.put("quantite", quantiteVendue);
                        return produitInfo;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<String, Object> rapport = new HashMap<>();
        rapport.put("nombreCommandes", nombreCommandes);
        rapport.put("chiffreAffaires", chiffreAffaires);
        rapport.put("topProduits", topProduits);

        return rapport;
    }
}
