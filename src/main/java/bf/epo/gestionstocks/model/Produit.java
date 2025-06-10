package bf.epo.gestionstocks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "La catégorie est obligatoire")
    @Column(nullable = false, length = 50)
    private String categorie;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @Min(value = 0, message = "Le prix unitaire doit être positif")
    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @NotNull(message = "La quantité en stock est obligatoire")
    @Min(value = 0, message = "La quantité doit être positive")
    @Column(name = "quantite_stock", nullable = false)
    private Integer quantiteStock;

    @NotNull(message = "Le seuil de réapprovisionnement est obligatoire")
    @Min(value = 0, message = "Le seuil doit être positif")
    @Column(name = "seuil_reapprovisionnement", nullable = false)
    private Integer seuilReapprovisionnement = 10;

    @Column(name = "ventes")
    private Integer ventes = 0;

    @Column(name = "date_ajout")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAjout = new Date();

    @Column(name = "image_url")
    private String imageUrl = "/images/produits/default.png";

    // Constructeurs
    public Produit() {
    }

    public Produit(String nom, String categorie, Double prixUnitaire, Integer quantiteStock, Integer seuilReapprovisionnement) {
        this.nom = nom;
        this.categorie = categorie;
        this.prixUnitaire = prixUnitaire;
        this.quantiteStock = quantiteStock;
        this.seuilReapprovisionnement = seuilReapprovisionnement;
    }

    public Produit(String nom, String categorie, Double prixUnitaire, Integer quantiteStock, Integer seuilReapprovisionnement, String imageUrl) {
        this.nom = nom;
        this.categorie = categorie;
        this.prixUnitaire = prixUnitaire;
        this.quantiteStock = quantiteStock;
        this.seuilReapprovisionnement = seuilReapprovisionnement;
        this.imageUrl = imageUrl;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(Integer quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public Integer getSeuilReapprovisionnement() {
        return seuilReapprovisionnement;
    }

    public void setSeuilReapprovisionnement(Integer seuilReapprovisionnement) {
        this.seuilReapprovisionnement = seuilReapprovisionnement;
    }

    public Integer getVentes() {
        return ventes;
    }

    public void setVentes(Integer ventes) {
        this.ventes = ventes;
    }

    public Date getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return String.format(
                "Produit{id=%d, nom='%s', categorie='%s', prix=%.2f, stock=%d, seuil=%d, ventes=%d}",
                id, nom, categorie, prixUnitaire, quantiteStock, seuilReapprovisionnement, ventes
        );
    }
}
