package eu.canpack.fip.bo.technologyCard;

import eu.canpack.fip.bo.technologyCard.TechnologyCard;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TechnologyCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechnologyCardRepository extends JpaRepository<TechnologyCard,Long>,JpaSpecificationExecutor<TechnologyCard> {

}
