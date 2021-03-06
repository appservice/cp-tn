package eu.canpack.fip.bo.cooperation;

import eu.canpack.fip.bo.cooperation.Cooperation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cooperation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CooperationRepository extends JpaRepository<Cooperation, Long>, JpaSpecificationExecutor<Cooperation> {

}
