package bf.malek.stockmanager.entity;

import jakarta.persistence.*;

@Entity
public class LigneCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Produit produit;

    @ManyToOne
    private Commande commande;

    private int quantite;
    private double prixUnitaire;

    public LigneCommande() {}

    public LigneCommande(Produit produit, Commande commande, int quantite, double prixUnitaire) {
        this.produit = produit;
        this.commande = commande;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public double getPrixTotal() {
        return this.quantite * this.prixUnitaire;
    }
    

    public Long getId() { return id; }
    public Produit getProduit() { return produit; }
    public Commande getCommande() { return commande; }
    public int getQuantite() { return quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }

    public void setId(Long id) { this.id = id; }
    public void setProduit(Produit produit) { this.produit = produit; }
    public void setCommande(Commande commande) { this.commande = commande; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
}
