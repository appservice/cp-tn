package eu.canpack.fip.bo.operator;

import eu.canpack.fip.bo.operator.Operator;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the Operator entity.
 */
@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long>, JpaSpecificationExecutor<Operator> {

    Optional<Operator> findOneByCardNumber(String cardNumber);
}
