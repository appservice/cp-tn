package eu.canpack.fip.bo.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CP S.A.
 * Created by lukasz.mochel on 25.02.2018.
 */
public interface AuditRepository extends JpaRepository<Audit, Long> {

    List<Audit> findAllByOrderId(Long orderId);
}
