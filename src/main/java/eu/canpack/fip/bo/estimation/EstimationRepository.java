package eu.canpack.fip.bo.estimation;

import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Estimation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstimationRepository extends JpaRepository<Estimation, Long> {


    @Query("Select e from Estimation e " +
        "join e.order o " +
        "where o.orderStatus<>eu.canpack.fip.bo.order.enumeration.OrderStatus.WORKING_COPY")
    Page<Estimation> findAllWhereOrderNotWorkingCopy(Pageable pageable);

    @Query(value = "select e from Estimation e " +
        "join e.order o " +
        "where o.estimationMaker =:loggedUser " +
//        "and (e.estimatedCost is null or e.estimatedCost=0)" +
        " ")
    Page<Estimation> findAllToFinish(Pageable pageable,
                                     @Param("loggedUser") User loggedUser);

}
