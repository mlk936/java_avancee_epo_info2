package bf.epo.gestionstocks.service;

import bf.epo.gestionstocks.exception.ResourceNotFoundException;
import bf.epo.gestionstocks.model.Produit;
import bf.epo.gestionstocks.repository.ProduitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProduitServiceTest {

    @Autowired
    private ProduitService produitService;

    @Autowired
    private ProduitRepository produitRepository;

    @BeforeEach
    void setup() {
        produitRepository.deleteAll();
        Produit p1 = new Produit("Test1", "Cat1", 10.0, 100, 10);
        Produit p2 = new Produit("Test2", "Cat2", 5.0, 5, 10);
        produitRepository.save(p1);
        produitRepository.save(p2);
    }

    @AfterEach
    void cleanup() {
        produitRepository.deleteAll();
    }

    @Test
    void testListerProduits() {
        List<Produit> liste = produitService.listerProduits();
        assertEquals(2, liste.size());
    }

    @Test
    void testGetProduitParId_Existe() {
        Produit p = produitRepository.findAll().get(0);
        Produit trouvé = produitService.getProduitParId(p.getId());
        assertNotNull(trouvé);
        assertEquals(p.getNom(), trouvé.getNom());
    }

    @Test
    void testGetProduitParId_NonExiste() {
        assertThrows(ResourceNotFoundException.class, () -> produitService.getProduitParId(999L));
    }

    @Test
    void testSupprimerProduit() {
        Produit p = produitRepository.findAll().get(0);
        produitService.supprimerProduit(p.getId());
        assertFalse(produitRepository.findById(p.getId()).isPresent());
    }
}
