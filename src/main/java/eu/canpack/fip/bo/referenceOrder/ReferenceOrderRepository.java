package eu.canpack.fip.bo.referenceOrder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * CP S.A.
 * Created by lukasz.mochel on 03.12.2017.
 */
public interface ReferenceOrderRepository extends JpaRepository<ReferenceOrder,Long>{

    Set<ReferenceOrder> findAllByRefOrderId(Long refOrderId);
}
