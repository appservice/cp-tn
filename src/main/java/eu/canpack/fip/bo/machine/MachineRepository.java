package eu.canpack.fip.bo.machine;

import eu.canpack.fip.bo.machine.Machine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the Machine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MachineRepository extends JpaRepository<Machine,Long> {


    @Query(" select new eu.canpack.fip.bo.machine.MachineDTO(m,dtl) from Machine m " +
        "left join m.machineDtls dtl " +
        "where (dtl.validFrom<=:operationDate or dtl.validFrom is null) " +
        "and (dtl.validTo>=:operationDate or dtl.validTo is NULL)")
    Page<MachineDTO> findAllByOperationDate(@Param("operationDate")LocalDate operationDate,Pageable pageable);

    @Query(" select new eu.canpack.fip.bo.machine.MachineDTO(m,dtl) from Machine m " +
        "left join m.machineDtls dtl " +
        "where (dtl.validFrom<=:operationDate or dtl.validFrom is null) " +
        "and (dtl.validTo>=:operationDate or dtl.validTo is NULL) " +
        "order by m.name asc ")
    List<MachineDTO> findAllByOperationDateNotPageable(@Param("operationDate")LocalDate operationDate);

    @Query(" select new eu.canpack.fip.bo.machine.MachineDTO(m,dtl) from Machine m " +
        "left join m.machineDtls dtl " +
        "where (dtl.validFrom<=:operationDate or dtl.validFrom is null) " +
        "and (dtl.validTo>=:operationDate or dtl.validTo is NULL) " +
        "and m.id in :machineIds " +
        "order by m.name asc ")
    List<MachineDTO> findAllByOperationDate(@Param("operationDate")LocalDate operationDate, @Param("machineIds") Set<Long> machineIds);


    @Query(" select new eu.canpack.fip.bo.machine.MachineDTO(m,dtl) from Machine m " +
        "left join m.machineDtls dtl " +
        "where (dtl.validFrom<=:operationDate or dtl.validFrom is null) " +
        "and (dtl.validTo>=:operationDate or dtl.validTo is NULL) " +
        "and m.id = :machineId "  )
    MachineDTO findOneByOperationDate(@Param("machineId") Long machineId, @Param("operationDate")LocalDate operationDate);

}
