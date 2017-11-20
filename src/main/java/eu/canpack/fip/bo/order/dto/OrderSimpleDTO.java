package eu.canpack.fip.bo.order.dto;

import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.domain.User;

import java.time.ZonedDateTime;

/**
 * CP S.A.
 * Created by lukasz.mochel on 19.08.2017.
 */
public class OrderSimpleDTO {
    private Long id;
    private String clientName;
    private String createdBy;
    private String internalNumber;
    private String referenceNumber;
    private OrderStatus orderStatus;
    private ZonedDateTime createdAt;

    public OrderSimpleDTO() {
    }

    public OrderSimpleDTO(Order order, Client client, User createdBy) {
        this.id = order.getId();
        this.clientName = client.getName();
        this.createdBy = createdBy.getFirstName()+" "+createdBy.getLastName();
        this.internalNumber = order.getInternalNumber();
        this.referenceNumber = order.getReferenceNumber();
        this.createdAt=order.getCreatedAt();
        this.orderStatus=order.getOrderStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "OrderSimpleDTO{" +
            "id=" + id +
            ", clientName='" + clientName + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", internalNumber='" + internalNumber + '\'' +
            ", referenceNumber='" + referenceNumber + '\'' +
            ", orderStatus='" + orderStatus + '\'' +

            '}';
    }
}
