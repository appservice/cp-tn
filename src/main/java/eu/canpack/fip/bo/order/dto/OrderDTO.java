package eu.canpack.fip.bo.order.dto;

import eu.canpack.fip.bo.estimation.EstimationCreateDTO;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import eu.canpack.fip.bo.estimation.EstimationDTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * CP S.A.
 * Created by lukasz.mochel on 04.08.2017.
 */
public class OrderDTO implements Serializable {

    private Long id;

    //    @NotNull
    private String internalNumber;

    private String sapNumber;

    private String referenceNumber;

    @NotNull
    private OrderType orderType;

    @NotNull
    private String name;

    private String description;

    private LocalDate closeDate;

    private OrderStatus orderStatus;

    private Long clientId;

    private String clientShortcut;

    private Long inquiryId;

    private List<EstimationCreateDTO> estimations = new ArrayList<>();

    public OrderDTO() {

    }

    public OrderDTO(Order o) {

        this.id = o.getId();
        this.internalNumber = o.getInternalNumber();
        this.sapNumber = o.getSapNumber();
        this.orderType = o.getOrderType();
        this.name = o.getName();
        this.description = o.getDescription();
        this.closeDate = o.getCloseDate();
        this.orderStatus = o.getOrderStatus();
        this.clientId = o.getClient().getId();
        this.clientShortcut=o.getClient().getShortcut();
        this.referenceNumber=o.getReferenceNumber();
       // this.estimations = estimations;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

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

    public List<EstimationCreateDTO> getEstimations() {
        return estimations;
    }

    public void setEstimations(List<EstimationCreateDTO> estimations) {
        this.estimations = estimations;
    }

    public String getClientShortcut() {
        return clientShortcut;
    }

    public void setClientShortcut(String clientShortcut) {
        this.clientShortcut = clientShortcut;
    }

    public Long getInquiryId() {
        return inquiryId;
    }

    public void setInquiryId(Long inquiryId) {
        this.inquiryId = inquiryId;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + id +
            ", internalNumber='" + internalNumber + '\'' +
            ", sapNumber='" + sapNumber + '\'' +
            ", orderType=" + orderType +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", closeDate=" + closeDate +
            ", orderStatus=" + orderStatus +
            ", clientId=" + clientId +
            ", estimations=" + estimations +
            ", inquiryId=" + inquiryId +
            '}';
    }
}
