package dauphine.fr.microservices.gestion_comptes.dauphine.fr.microservices.gestion_comptes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

interface CompteBancaireRepository extends JpaRepository<CompteBancaire, Integer> {
}