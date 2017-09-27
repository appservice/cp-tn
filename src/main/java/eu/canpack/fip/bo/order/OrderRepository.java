package eu.canpack.fip.bo.order;

import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import eu.canpack.fip.bo.order.dto.OrderSimpleDTO;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import eu.canpack.fip.bo.order.Order;


/**
 * Spring Data JPA repository for the Order entity.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order,Long>,JpaSpecificationExecutor<Order> {
    @Query("select coalesce(max(number), '0') as max_number from Order where year = ?1")
    int getDocNumber(int year);


    @Query("select new eu.canpack.fip.bo.order.dto.OrderDTO(o) from Order o " +
        "where o.id=:id")
    OrderDTO findOrderById(@Param("id") Long id);


    @Query("select new eu.canpack.fip.bo.order.dto.OrderSimpleDTO(o,c,u) from Order o " +
        "left join o.client c " +
        "left join o.createdBy u " +
        "where o.id=:orderId")
    OrderSimpleDTO getOrderSimpleDTO(@Param("orderId") Long orderId);

    @Query("select o from Order o " +
        "where o.estimationMaker is null " +
        "and o.orderStatus<>eu.canpack.fip.bo.order.enumeration.OrderStatus.WORKING_COPY")
    Page<Order> findOrdersClaimToEstimation(Pageable pageable);

    Page<Order> findAllByClientAndOrderType(Client client, OrderType orderType, Pageable pageable);

    Page<Order> findAllByOrderType(OrderType orderType, Pageable pageable);

    @Query("select o from Order o " +
        "where o.estimationMaker.id=:currentUserId " +
        "and o.orderStatus=eu.canpack.fip.bo.order.enumeration.OrderStatus.IN_ESTIMATION")
    Page<Order> findOrderToEstimationByUser(@Param("currentUserId") Long currentUserId, Pageable pageable);


}
