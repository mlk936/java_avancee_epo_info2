package bf.epo.gestionstocks.controller;

import bf.epo.gestionstocks.model.Produit;
import bf.epo.gestionstocks.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AccueilController {

    @Autowired
    private ProduitService produitService;

    @GetMapping("/")
    public String afficherAccueil(Model model) {
        List<Produit> produitsVedettes = produitService.getProduitsVedettes();
        List<Produit> nouveautes = produitService.getNouveauxProduits();

        model.addAttribute("produitsVedettes", produitsVedettes);
        model.addAttribute("nouveautes", nouveautes);

        return "accueil"; // accueil.html (Thymeleaf) dans templates/
    }
}
