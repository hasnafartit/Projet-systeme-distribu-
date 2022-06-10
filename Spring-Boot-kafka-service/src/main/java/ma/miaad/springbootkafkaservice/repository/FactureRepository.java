package ma.miaad.springbootkafkaservice.repository;

import ma.miaad.springbootkafkaservice.Entity.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FactureRepository extends JpaRepository<Facture,Long> {
}
