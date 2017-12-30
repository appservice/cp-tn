package eu.canpack.fip.repository;

import eu.canpack.fip.domain.MpkBudgetMapper;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MpkBudgetMapper entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MpkBudgetMapperRepository extends JpaRepository<MpkBudgetMapper, Long> {

}
