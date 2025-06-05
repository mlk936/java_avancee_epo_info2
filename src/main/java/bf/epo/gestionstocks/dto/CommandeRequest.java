package bf.epo.gestionstocks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class CommandeRequest {

    @NotBlank(message = "Le nom du client est obligatoire")
    private String nomClient;

    @NotEmpty(message = "La liste des lignes de commande ne peut pas être vide")
    private List<LigneCommandeRequest> lignes;

    public CommandeRequest() {}

    public String getNomClient() {
        return nomClient;
    }
    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public List<LigneCommandeRequest> getLignes() {
        return lignes;
    }
    public void setLignes(List<LigneCommandeRequest> lignes) {
        this.lignes = lignes;
    }
}
