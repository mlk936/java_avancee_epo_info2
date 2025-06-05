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
     * Crée une commande à partir d’une CommandeRequest (DTO),
     * vérifie le stock et ajuste automatiquement les quantités.
     * Tout est dans une transaction : soit tout est validé, soit tout rollback.
     */
    @Transactional
    public Commande creerCommande(CommandeRequest request) {
        // 1. Créer l’entité Commande
        Commande commande = new Commande();
        commande.setNomClient(request.getNomClient());
        commande.setDateCommande(LocalDate.now());

        // 2. Vérifier le stock pour chaque ligne demandée
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

        // 3. Sauvegarder la commande (sans lignes pour l’instant, cascade persistance)
        Commande savedCommande = commandeRepository.save(commande);

        // 4. Pour chaque ligne, créer LigneCommande, l’attacher à la commande, mettre à jour le stock
        List<LigneCommande> lignesToSave = new ArrayList<>();
        for (LigneCommandeRequest lcReq : request.getLignes()) {
            Produit p = produitRepository.findById(lcReq.getProduitId()).get();
            LigneCommande lc = new LigneCommande();
            lc.setProduit(p);
            lc.setQuantite(lcReq.getQuantite());
            lc.setPrixUnitaire(p.getPrixUnitaire());
            lc.setCommande(savedCommande);
            lignesToSave.add(lc);

            // Mise à jour du stock du produit
            p.setQuantiteStock(p.getQuantiteStock() - lcReq.getQuantite());
            produitRepository.save(p);
        }

        ligneCommandeRepository.saveAll(lignesToSave);

        // 5. Retourner la commande avec ses lignes
        savedCommande.setLignes(lignesToSave);
        return savedCommande;
    }

    /**
     * Génère une facture texte pour une commande existante. 
     * Retourne le contenu de la facture (String) : peut être stocké en .txt par le contrôleur.
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

    /**
     * Récupère la liste des commandes pour une date donnée (exemple : aujourd’hui).
     */
    public List<Commande> getCommandesParDate(LocalDate date) {
        return commandeRepository.findByDateCommande(date);
    }

    /**
     * Pour le rapport du jour : nombre de commandes, chiffre d’affaires, top 5 produits vendus.
     * Retourne une map contenant :
     *  - "nbCommandes" -> Integer
     *  - "chiffreAffaires" -> Double
     *  - "topProduits" -> List<Map.Entry<Produit, Integer>> (produit + quantité vendue)
     */
    public Map<String, Object> genererRapportDuJour(LocalDate date) {
        List<Commande> commandes = getCommandesParDate(date);
        int nbCommandes = commandes.size();

        // Calcul du chiffre d’affaires total
        double chiffreAffaires = commandes.stream()
                .flatMap(c -> ligneCommandeRepository.findByCommandeId(c.getId()).stream())
                .mapToDouble(lc -> lc.getQuantite() * lc.getPrixUnitaire())
                .sum();

        // Top 5 produits vendus (par somme des quantités)
        Map<Produit, Integer> ventesMap = new HashMap<>();
        for (Commande c : commandes) {
            List<LigneCommande> lignes = ligneCommandeRepository.findByCommandeId(c.getId());
            for (LigneCommande lc : lignes) {
                ventesMap.merge(lc.getProduit(), lc.getQuantite(), Integer::sum);
            }
        }
        List<Map.Entry<Produit, Integer>> topProduits = ventesMap.entrySet().stream()
                .sorted(Map.Entry.<Produit, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        Map<String, Object> rapport = new HashMap<>();
        rapport.put("nbCommandes", nbCommandes);
        rapport.put("chiffreAffaires", chiffreAffaires);
        rapport.put("topProduits", topProduits);
        return rapport;
    }
}
