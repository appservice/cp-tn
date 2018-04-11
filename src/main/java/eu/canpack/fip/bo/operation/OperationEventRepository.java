package eu.canpack.fip.bo.operation;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CP S.A.
 * Created by lukasz.mochel on 07.04.2018.
 */
public interface OperationEventRepository extends JpaRepository<OperationEvent,Long> {
}
