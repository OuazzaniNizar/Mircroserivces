package dauphine.fr.microservices.gestion_transactions.dauphine.fr.microservices.gestion_transactions;

public enum TypeTransaction {
    VIREMENT {
        public String toString() {
            return "Virement";
        }
    },

    RETRAIT {
        public String toString() {
            return "Retrait";
        }
    }
}