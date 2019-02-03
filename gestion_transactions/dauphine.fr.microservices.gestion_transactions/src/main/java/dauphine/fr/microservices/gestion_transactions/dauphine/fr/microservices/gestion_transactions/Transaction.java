package dauphine.fr.microservices.gestion_transactions.dauphine.fr.microservices.gestion_transactions;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Transaction {


    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;


    @Column(name="emetteur")
    private Integer emetteur;

    @Column(name="recepteur")
    private Integer recepteur;

    @Column(name="intitule")
    private String intitule;

    @Column(name="date_transaction")
    @CreationTimestamp
    private Date date_transaction;

    @Column(name="montant")
    private float montant;

    public Transaction() {
    }


    public Transaction(Integer emetteur, Integer recepteur, String intitule, float montant) {
        this.emetteur = emetteur;
        this.recepteur = recepteur;
        this.intitule = intitule;
        this.montant = montant;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(Integer emetteur) {
        this.emetteur = emetteur;
    }

    public Integer getRecepteur() {
        return recepteur;
    }

    public void setRecepteur(Integer recepteur) {
        this.recepteur = recepteur;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

}
