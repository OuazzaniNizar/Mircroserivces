package dauphine.fr.microservices.gestion_comptes.dauphine.fr.microservices.gestion_comptes;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.ribbon.proxy.annotation.Http;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/comptes")
public class CompteBancaireRestController {

    public static final Logger logger = LoggerFactory.getLogger(CompteBancaireRestController.class);

    @Autowired
    CompteBancaireRepository repo;

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("")
    public ResponseEntity<List<CompteBancaire>>  listerLesComptes(){
        List<CompteBancaire> comptes=  repo.findAll();
        if (comptes.isEmpty()) {
            return new ResponseEntity("Pas de compte présent dans la base.",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<CompteBancaire>>(comptes, HttpStatus.OK);
    }

    @GetMapping(value = "/{iban:[0-9]+}")
    public ResponseEntity<?> recupererCompteParIban(@PathVariable("iban") Long iban) {
        Optional<CompteBancaire> compte = repo.findById(iban);
        if (!compte.isPresent()) {
            return new ResponseEntity<>("Le compte ayant l'iban "+iban+" n'est pas présent dans la base.",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(compte, HttpStatus.OK);
    }

    @DeleteMapping("/{iban:[0-9]+}")
    @ResponseBody
    public ResponseEntity<Object> supprimerCompteParIban(@PathVariable Long iban){

        Optional<CompteBancaire> compte = repo.findById(iban);

        if (compte.isPresent()){
            repo.deleteById(iban);
            return new ResponseEntity<>("Le compte "+compte.get().getIban()+" a été supprimé.", HttpStatus.OK);

        }
            return new ResponseEntity<>("Le compte "+compte.get().getIban()+" est introuvable.", HttpStatus.NOT_FOUND);

    }


    @PostMapping("")
    public ResponseEntity<Object> creerCompte(@RequestParam(value = "frais_tenue_compte", required = true) float frais_tenue_compte,
                                              @RequestParam(value = "interet_compte", required = true)float interet_compte,
                                              @RequestParam(value = "type_compte", required = true)String type_compte,
                                              @RequestParam(value = "solde_initial", required = true)float solde_initial) {

        CompteBancaire nouveauCompte = repo.save(new CompteBancaire(type_compte,frais_tenue_compte,solde_initial,interet_compte, new Date(),true));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(nouveauCompte.getIban()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{iban:[0-9]+}")
    public ResponseEntity<Object> majCompteParIban(@RequestParam(value = "frais_tenue_compte", required = true) float frais_tenue_compte,
                                                   @RequestParam(value = "interet_compte", required = true)float interet_compte,
                                                   @PathVariable("iban") Long iban) {

        Optional<CompteBancaire> ancienCompte = repo.findById(iban);

        if (!ancienCompte.isPresent())
            return ResponseEntity.notFound().build();

        ancienCompte.get().setFrais_tenue_compte(frais_tenue_compte);
        ancienCompte.get().setInteret_compte(interet_compte);

        repo.save(ancienCompte.get());

        return new ResponseEntity<>(ancienCompte.get(), HttpStatus.OK);
    }


    @PutMapping("/appliquer_interet/{iban:[0-9]+}")
    public ResponseEntity<Object> appliquerInteret(@PathVariable("iban") Long iban) {

        Optional<CompteBancaire> compte = repo.findById(iban);

        if (!compte.isPresent())
            return ResponseEntity.notFound().build();

        compte.get().setSolde(compte.get().getSolde()*compte.get().getInteret_compte());

        repo.save(compte.get());

        Application application = eurekaClient.getApplication("gestion_transactions");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String url = "http://"+instanceInfo.getIPAddr()+ ":"+instanceInfo.getPort()+"/"+"transactions/";
        HttpEntity<Transaction> request = new HttpEntity<>(new Transaction(HttpStatus.CREATED, (long) 0000,iban,"intérêt",compte.get().getSolde()*compte.get().getInteret_compte()));
        restTemplate.postForObject(url, request, Transaction.class);

        return new ResponseEntity<>(compte, HttpStatus.OK);
    }


    @PutMapping("/operations/virement/")
    public ResponseEntity<Object> virement(@RequestParam(value = "iban_emetteur", required = true) Long iban_emetteur,
                                           @RequestParam(value = "iban_recepteur", required = true)Long iban_recepteur,
                                           @RequestParam(value = "montant", required = true) float montant_transaction) {

        Optional<CompteBancaire> compteEmetteur = repo.findById(iban_emetteur);
        Optional<CompteBancaire> compteRecepteur = repo.findById(iban_recepteur);

        if (!compteEmetteur.isPresent() || !compteRecepteur.isPresent())
            return new ResponseEntity<>("compteEmetteur ou compteRecepteur introuvable.", HttpStatus.NOT_FOUND);

        if(compteEmetteur.get().getSolde()<0 || compteEmetteur.get().getSolde() - montant_transaction<0)
            return new ResponseEntity<>("Solde insuffisant dans le compte émetteur.", HttpStatus.METHOD_NOT_ALLOWED);
        //AJOUTER AUTORISATION DE DECOUVERT ET VERIFIER
        compteEmetteur.get().setSolde(compteEmetteur.get().getSolde() - montant_transaction);
        compteRecepteur.get().setSolde(compteRecepteur.get().getSolde() + montant_transaction);

        repo.save(compteEmetteur.get());
        repo.save(compteRecepteur.get());

        Application application = eurekaClient.getApplication("gestion_transactions");
        InstanceInfo instanceInfo = application.getInstances().get(0);

        String url = "http://"+instanceInfo.getIPAddr()+ ":"+instanceInfo.getPort()+"/"+"transactions/";
        HttpEntity<Transaction> request = new HttpEntity<>(new Transaction(HttpStatus.CREATED,iban_emetteur,iban_recepteur,"virement",montant_transaction));
        restTemplate.postForObject(url, request, Transaction.class);


        return new ResponseEntity<>("Virement émis avec succès.", HttpStatus.CREATED);
    }


    @PutMapping("/operations/depot_retrait/")
    public ResponseEntity<Object> depot_retrait(@RequestParam("iban") Long iban,
                                        @RequestParam(value = "montant", required = true) float montant,
                                        @RequestParam(value = "type_opération", required = true) String type_opération) {

        Optional<CompteBancaire> compteEmetteur = repo.findById(iban);

        if (!compteEmetteur.isPresent())
            return new ResponseEntity<>("compteEmetteur ou compteRecepteur introuvable.", HttpStatus.NOT_FOUND);

        if(!type_opération.equals("dépôt") && !type_opération.equals("retrait")){
            return new ResponseEntity<>("L'opération doit être un dépôt ou un retrait.", HttpStatus.METHOD_NOT_ALLOWED);
        }
        if( montant<0 && type_opération.equals("dépôt"))
            return new ResponseEntity<>("Montant déposé doit être positif.", HttpStatus.METHOD_NOT_ALLOWED);

        if( montant>0 && type_opération.equals("retrait"))
            return new ResponseEntity<>("Montant retiré doit être négatif.", HttpStatus.METHOD_NOT_ALLOWED);


        compteEmetteur.get().setSolde(compteEmetteur.get().getSolde() + montant);

        repo.save(compteEmetteur.get());

        Application application = eurekaClient.getApplication("gestion_transactions");
        InstanceInfo instanceInfo = application.getInstances().get(0);

        String url = "http://"+instanceInfo.getIPAddr()+ ":"+instanceInfo.getPort()+"/"+"transactions/";
        HttpEntity<Transaction> request = new HttpEntity<>(new Transaction(HttpStatus.CREATED,iban,iban,"dépôt",montant));

        ResponseEntity<Transaction> response= restTemplate.postForObject(url,request, Transaction.class);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Data
    public class Transaction extends ResponseEntity<Transaction> {
        private Long emetteur;
        private Long recepteur;
        private String intitule;
        private float montant;

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
}
