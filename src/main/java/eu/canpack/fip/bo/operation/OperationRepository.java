package eu.canpack.fip.bo.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the Operation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query("select distinct o from Operation  o " +
        "left join fetch o.operationEvents " +
        "where o.estimation.id =:estimationId")
    List<Operation> findOperationReportByEstimationId(@Param("estimationId") Long estimationId);

    List<Operation> findAllByEstimationId(Long estimationId);

}
