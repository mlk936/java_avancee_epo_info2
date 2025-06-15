package bf.epo.gestionstocks.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commande")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du client est obligatoire")
    @Column(name = "nom_client", nullable = false, length = 100)
    private String nomClient;

    @NotNull(message = "La date de commande est obligatoire")
    @Column(name = "date_commande", nullable = false)
    private LocalDate dateCommande;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<LigneCommande> lignes = new ArrayList<>();


    public Commande() { }

    public Commande(String nomClient, LocalDate dateCommande) {
        this.nomClient = nomClient;
        this.dateCommande = dateCommande;
    }


    public Long getId() {
        return id;
    }

    public String getNomClient() {
        return nomClient;
    }
    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }
    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public List<LigneCommande> getLignes() {
        return lignes;
    }
    public void setLignes(List<LigneCommande> lignes) {
        this.lignes = lignes;
    }

    public void ajouterLigne(LigneCommande ligne) {
        lignes.add(ligne);
        ligne.setCommande(this);
    }

    public void retirerLigne(LigneCommande ligne) {
        lignes.remove(ligne);
        ligne.setCommande(null);
    }

    @Override
    public String toString() {
        return String.format("Commande{id=%d, client='%s', date=%s, nbLignes=%d}",
                id, nomClient, dateCommande, lignes.size());
    }
}
