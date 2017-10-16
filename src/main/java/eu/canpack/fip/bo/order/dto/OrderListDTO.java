package eu.canpack.fip.bo.order.dto;


import eu.canpack.fip.bo.order.enumeration.OrderType;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Order entity.
 */
public class OrderListDTO implements Serializable {

    private Long id;

//    @NotNull
    private String internalNumber;

    private String sapNumber;

    private String referenceNumber;

    @NotNull
    private OrderType orderType;


    private String name;

    private String description;

    private LocalDate closeDate;

    private OrderStatus orderStatus;

    @NotNull
    private Long clientId;

    private String clientShortcut;

    private ZonedDateTime createdAt;

    private List<String> buttons = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getSapNumber() {
        return sapNumber;
    }

    public void setSapNumber(String sapNumber) {
        this.sapNumber = sapNumber;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientShortcut() {
        return clientShortcut;
    }

    public void setClientShortcut(String clientShortcut) {
        this.clientShortcut = clientShortcut;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public List<String> getButtons() {
        return buttons;
    }

    public void setButtons(List<String> buttons) {
        this.buttons = buttons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderListDTO orderListDTO = (OrderListDTO) o;
        if(orderListDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderListDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderListDTO{" +
            "id=" + getId() +
            ", internalNumber='" + getInternalNumber() + "'" +
            ", sapNumber='" + getSapNumber() + "'" +
            ", orderType='" + getOrderType() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", closeDate='" + getCloseDate() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", clientShortcut='" + getClientShortcut() + "'" +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            "}";
    }
}
