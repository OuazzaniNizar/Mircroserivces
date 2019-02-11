package dauphine.fr.microservices.gestion_comptes.dauphine.fr.microservices.gestion_comptes;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class Transaction extends ResponseEntity<Transaction> {
    private Long emetteur;
    private Long recepteur;
    private String intitule;
    private float montant;

    public Transaction(){
        super(HttpStatus.OK);
    }

    public Transaction(HttpStatus status, Long emetteur, Long recepteur, String intitule, float montant) {
        super(status);
        this.emetteur = emetteur;
        this.recepteur = recepteur;
        this.intitule = intitule;
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "emetteur=" + emetteur +
                ", recepteur=" + recepteur +
                ", intitule='" + intitule + '\'' +
                ", montant=" + montant +
                '}';
    }
}
