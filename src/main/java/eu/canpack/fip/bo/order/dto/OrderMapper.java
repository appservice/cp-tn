package eu.canpack.fip.bo.order.dto;

import eu.canpack.fip.bo.estimation.dto.EstimationCreateMapper;
import eu.canpack.fip.bo.client.ClientMapper;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import eu.canpack.fip.bo.order.dto.OrderListDTO;
import eu.canpack.fip.service.dto.UserShortDTO;
import eu.canpack.fip.service.mapper.EntityMapper;
import eu.canpack.fip.service.mapper.UserShortMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity Order and its DTO OrderListDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, EstimationCreateMapper.class, UserShortMapper.class})
public interface OrderMapper extends EntityMapper<OrderListDTO, Order> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.shortcut", target = "clientShortcut")
    @Mapping(target = "buttons", ignore = true)
    OrderListDTO toDto(Order order);

    @Mapping(target = "estimations", ignore = true)
    @Mapping(source = "clientId", target = "client")
    @Mapping(target = "estimationMaker", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Order toEntity(OrderListDTO orderListDTO);

    default Order fromId(Long id) {
        if (id == null) {
            return null;
        }
        Order order = new Order();
        order.setId(id);
        return order;
    }


    @Mapping(source = "clientId", target = "client")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "closeDate", ignore = true)
    @Mapping(target = "estimations", ignore = true)
    Order toEntity(OrderDTO orderDTO);


//    OrderDTO toDTO(Order order);

}
