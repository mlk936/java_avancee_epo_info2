package bf.epo.gestionstocks.controller;
import bf.epo.gestionstocks.dto.CommandeRequest;
import bf.epo.gestionstocks.model.Commande;
import bf.epo.gestionstocks.service.CommandeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final CommandeService commandeService;

    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    @PostMapping
    public ResponseEntity<Commande> createCommande(@Valid @RequestBody CommandeRequest request) {
        Commande created = commandeService.creerCommande(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}/facture")
    public ResponseEntity<String> getFacture(@PathVariable Long id) {
        String facture = commandeService.genererFacture(id);
        return ResponseEntity.ok(facture);
    }

    @GetMapping
    public ResponseEntity<List<Commande>> getCommandesByDate(
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Commande> commandes = commandeService.getCommandesParDate(date);
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/rapport")
    public ResponseEntity<Map<String, Object>> getRapportDuJour(
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        Map<String, Object> rapport = commandeService.genererRapportDuJour(date);
        return ResponseEntity.ok(rapport);
    }
}
