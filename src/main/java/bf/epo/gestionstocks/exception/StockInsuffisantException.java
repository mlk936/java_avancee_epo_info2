package bf.epo.gestionstocks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Renvoyé lorsqu’on essaie de créer une commande avec une quantité > stock disponible.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StockInsuffisantException extends RuntimeException {
    public StockInsuffisantException(String message) {
        super(message);
    }
}
