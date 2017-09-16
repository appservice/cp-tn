package eu.canpack.fip.repository;

import eu.canpack.fip.bo.commercialPart.CommercialPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the CommercialPart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommercialPartRepository extends JpaRepository<CommercialPart,Long> {

}
