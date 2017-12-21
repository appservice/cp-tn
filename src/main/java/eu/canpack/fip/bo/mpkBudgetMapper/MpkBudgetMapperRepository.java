package eu.canpack.fip.bo.mpkBudgetMapper;

import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.mpkBudgetMapper.MpkBudgetMapper;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the MpkBudgetMapper entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MpkBudgetMapperRepository extends JpaRepository<MpkBudgetMapper, Long> {

    Optional<MpkBudgetMapper> findOneByMpkIgnoreCaseAndClientId(String mpk, Long clientId);

}
