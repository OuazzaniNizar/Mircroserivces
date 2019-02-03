package dauphine.fr.microservices.gestion_comptes.dauphine.fr.microservices.gestion_comptes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CompteBancaire {


    @Id /*@GeneratedValue(strategy = GenerationType.AUTO)*/
    @Column(name="iban")
    private Integer iban;

    /*Compte bancaire courant
    Compte épargne, aussi appelé compte sur livret
    Compte à terme ’argent déposé sur le compte est immobilisé pendant la durée du contrat, souvent de plusieurs années.
    L’intérêt est justement que plus l’argent reste longtemps sur le compte, plus le taux est élevé.
    */
    @Column(name="type_compte")
    private String type_compte;

    @Column(name="frais_tenue_compte")
    private float frais_tenue_compte;

    @Column(name="solde")
    private float solde;

    @Column(name="interet_compte")
    private float interet_compte;

    @Column(name="date_creation_compte")
    private String date_creation_compte;

    //actif - inactif
    @Column(name="etat_compte")
    private boolean etat_compte;


    public CompteBancaire() {
    }

    public CompteBancaire(Integer iban, String type_compte, float frais_tenue_compte, float solde, float interet_compte, String date_creation_compte, boolean etat_compte) {
        this.iban = iban;
        this.type_compte = type_compte;
        this.frais_tenue_compte = frais_tenue_compte;
        this.solde=solde;
        this.interet_compte = interet_compte;
        this.date_creation_compte = date_creation_compte;
        this.etat_compte = etat_compte;
    }

    public Integer getIban() {
        return iban;
    }

    public void setIban(Integer iban) {
        this.iban = iban;
    }

    public float getSolde() {
        return solde;
    }

    public void setSolde(float solde) {
        this.solde = solde;
    }

    public String getType_compte() {
        return type_compte;
    }

    public void setType_compte(String type_compte) {
        this.type_compte = type_compte;
    }

    public float getFrais_tenue_compte() {
        return frais_tenue_compte;
    }

    public void setFrais_tenue_compte(float frais_tenue_compte) {
        this.frais_tenue_compte = frais_tenue_compte;
    }

    public float getInteret_compte() {
        return interet_compte;
    }

    public void setInteret_compte(float interet_compte) {
        this.interet_compte = interet_compte;
    }

    public String getDate_creation_compte() {
        return date_creation_compte;
    }

    public void setDate_creation_compte(String date_creation_compte) {
        this.date_creation_compte = date_creation_compte;
    }

    public boolean isEtat_compte() {
        return etat_compte;
    }

    public void setEtat_compte(boolean etat_compte) {
        this.etat_compte = etat_compte;
    }

}
