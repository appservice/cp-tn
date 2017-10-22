package eu.canpack.fip.bo.machine;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CP S.A.
 * Created by lukasz.mochel on 22.10.2017.
 */
public interface MachineDtlRepository extends JpaRepository<MachineDtl, Long>{

    List<MachineDtl> findAllByMachineId(Long machineId);
}
