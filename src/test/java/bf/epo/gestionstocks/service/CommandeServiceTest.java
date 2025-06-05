package bf.epo.gestionstocks.service;

import bf.epo.gestionstocks.dto.CommandeRequest;
import bf.epo.gestionstocks.dto.LigneCommandeRequest;
import bf.epo.gestionstocks.exception.StockInsuffisantException;
import bf.epo.gestionstocks.model.Produit;
import bf.epo.gestionstocks.repository.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CommandeServiceTest {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CommandeService commandeService;

    private Produit produit;

    @BeforeEach
    void setup() {
        produitRepository.deleteAll();
        produit = new Produit("ProdTest", "CatTest", 10.0, 2, 1);
        produitRepository.save(produit);
    }

    @Test
    void testCreerCommande_StockInsuffisant() {
        LigneCommandeRequest lcReq = new LigneCommandeRequest();
        lcReq.setProduitId(produit.getId());
        lcReq.setQuantite(5); // Stock insuffisant

        CommandeRequest cmdReq = new CommandeRequest();
        cmdReq.setNomClient("ClientTest");
        cmdReq.setLignes(Collections.singletonList(lcReq));

        assertThrows(StockInsuffisantException.class, () -> commandeService.creerCommande(cmdReq));
    }

    @Test
    void testCreerCommande_Valide() {
        LigneCommandeRequest lcReq = new LigneCommandeRequest();
        lcReq.setProduitId(produit.getId());
        lcReq.setQuantite(1);

        CommandeRequest cmdReq = new CommandeRequest();
        cmdReq.setNomClient("ClientOK");
        cmdReq.setLignes(Collections.singletonList(lcReq));

        var commande = commandeService.creerCommande(cmdReq);

        assertNotNull(commande.getId());
        assertEquals("ClientOK", commande.getNomClient());
        assertEquals(1, commande.getLignes().size());

        Produit updated = produitRepository.findById(produit.getId()).orElseThrow();
        assertEquals(1, updated.getQuantiteStock());
    }
}
