package dauphine.fr.microservices.gestion_comptes.dauphine.fr.microservices.gestion_comptes;

public enum TypeCompte {
    DEPOT {
        public String toString() {
            return "Compte de dépôt";
        }
    },

    LIVRET_A {
        public String toString() {
            return "Compte sur livret - A";
        }
    }
}