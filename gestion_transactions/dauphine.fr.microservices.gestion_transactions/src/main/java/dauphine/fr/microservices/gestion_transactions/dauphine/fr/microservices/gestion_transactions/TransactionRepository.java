package dauphine.fr.microservices.gestion_transactions.dauphine.fr.microservices.gestion_transactions;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction,Integer>{

    @Query("SELECT tr FROM Transaction tr WHERE tr.emetteur = :iban_emetteur")
    List<Transaction> rechercheTransctionEmetteur(@Param("iban_emetteur") Long iban);

    @Query("SELECT tr FROM Transaction tr WHERE tr.recepteur = :iban_recepteur")
    List<Transaction> rechercheTransctionRecepteur(@Param("iban_recepteur") Long iban);



}
