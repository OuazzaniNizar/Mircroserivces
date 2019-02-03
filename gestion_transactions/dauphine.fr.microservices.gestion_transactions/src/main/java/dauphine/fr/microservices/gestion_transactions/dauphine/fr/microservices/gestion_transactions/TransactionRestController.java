package dauphine.fr.microservices.gestion_transactions.dauphine.fr.microservices.gestion_transactions;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionRestController {


    @Autowired
    TransactionRepository repo;

    //get all accounts
    @GetMapping("")
    public ResponseEntity<List<Transaction>> listerTransactions(){
        List<Transaction> transactions=  repo.findAll();
        if (transactions.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }

    @GetMapping(value = "/{id:[0-9]+}")
    public ResponseEntity<?> recupererTransactionParId(@PathVariable("id") Integer id) {
        Optional<Transaction> transaction = repo.findById(id);

        if (!transaction.isPresent()) {
            return new ResponseEntity<Transaction>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PutMapping(value = "/{id:[0-9]+}")
    public ResponseEntity<?> majTransactionParId(@RequestBody Transaction majTransaction,@PathVariable("id") Integer id) {
        Optional<Transaction> ancienneTransaction = repo.findById(id);

        if (!ancienneTransaction.isPresent()) {
            return new ResponseEntity<Transaction>(HttpStatus.NOT_FOUND);
        }
        majTransaction.setId(id);

        repo.save(majTransaction);

        return new ResponseEntity<>(majTransaction, HttpStatus.OK);
    }


    @DeleteMapping("/{id:[0-9]+}")
    @ResponseBody
    public ResponseEntity<?> supprimerTransactionParId(@PathVariable Integer id){
        Optional<Transaction> transaction = repo.findById(id);
        if (!transaction.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        repo.deleteById(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);

    }


    @PostMapping("")
    public ResponseEntity<Object> ajouterTransaction(@RequestBody Transaction transaction) {
        Transaction nouvelleTransaction = repo.save(transaction);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(nouvelleTransaction.getId()).toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(nouvelleTransaction, httpHeaders, HttpStatus.CREATED);
    }



    @GetMapping("/recherche/emetteur/{iban_emetteur:[0-9]+}")
    public ResponseEntity<List<Transaction>> rechercherTransactionEmetteur(@PathVariable("iban_emetteur") Integer iban_emetteur) {
        List<Transaction> transactions= repo.rechercheTransctionEmetteur(iban_emetteur);

        if (transactions.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }

    @GetMapping("/recherche/recepteur/{iban_recepteur:[0-9]+}")
    public ResponseEntity<List<Transaction>> rechercherTransactionRecepteur(@PathVariable("iban_recepteur") Integer iban_recepteur) {
        List<Transaction> transactions= repo.rechercheTransctionRecepteur(iban_recepteur);

        if (transactions.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }
}

