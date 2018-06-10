package eu.canpack.fip.bo.operation;

import eu.canpack.fip.bo.operation.dto.CurrentOperationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * CP S.A.
 * Created by lukasz.mochel on 07.04.2018.
 */
public interface OperationEventRepository extends JpaRepository<OperationEvent, Long> {

    @Query("select new eu.canpack.fip.bo.operation.dto.CurrentOperationDTO(oe) from OperationEvent oe " +
        "join oe.operation o " +
        "where o.operationStatus=eu.canpack.fip.bo.operation.enumeration.OperationStatus.STARTED or " +
        "o.operationStatus=eu.canpack.fip.bo.operation.enumeration.OperationStatus.RESUMED")
    List<CurrentOperationDTO> findCurrentOperations();
}
