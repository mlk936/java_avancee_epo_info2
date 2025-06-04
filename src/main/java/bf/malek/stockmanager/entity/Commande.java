package bf.malek.stockmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import jakarta.persistence.CascadeType;
import java.util.List;



@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String client;
    private LocalDate dateCommande;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<LigneCommande> lignes;

    public Commande() {
        this.dateCommande = LocalDate.now();
    }

    public Commande(String client, List<LigneCommande> lignes) {
        this.client = client;
        this.dateCommande = LocalDate.now();
        this.lignes = lignes;
    }

    public Long getId() { 
        return id; 
    }
    public String getClient() { 
        return client; 
    }
    public LocalDate getDateCommande() { 
        return dateCommande; 
    }
    public List<LigneCommande> getLignes() { 
        return lignes; 
    }


    public void setId(Long id) { 
        this.id = id; 
    }
    public void setClient(String client) {
         this.client = client; 
    }
    public void setDateCommande(LocalDate dateCommande) {
         this.dateCommande = dateCommande; 
    }
    public void setLignes(List<LigneCommande> lignes) { 
        this.lignes = lignes; 
    }

    public double getTotal() {
        return lignes != null ? lignes.stream()
                .mapToDouble(LigneCommande::getPrixTotal)
                .sum() : 0.0;
    }
}
