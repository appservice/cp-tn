package eu.canpack.fip.bo.order.dto;

import eu.canpack.fip.bo.estimation.dto.EstimationCreateDTO;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.bo.order.enumeration.OrderType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
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

    private String name;

    private String description;

    private LocalDate closeDate;

    private OrderStatus orderStatus;

    private Long clientId;

    private String clientShortcut;

    private Long inquiryId;

    private ZonedDateTime createdAt;

    private String createdByName;

    private String estimationMakerName;

    private List<EstimationCreateDTO> estimations = new ArrayList<>();

    private ZonedDateTime estimationFinishDate;

    private String offerRemarks;

    private String deliveryAddress;

    private boolean canEdit;

    private boolean canEditAsAdmin;

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
        this.createdAt=o.getCreatedAt();
        if(o.getCreatedBy()!=null) {
            this.createdByName = o.getCreatedBy().getFirstName() + " " + o.getCreatedBy().getLastName();
        }
        if(o.getEstimationMaker()!=null) {
            this.estimationMakerName = o.getEstimationMaker().getFirstName() + " " + o.getEstimationMaker().getLastName();
        }
        this.estimationFinishDate=o.getEstimationFinsihDate();
        this.offerRemarks=o.getOfferRemarks();
        this.deliveryAddress=o.getDeliveryAddress();


       // this.estimations = estimations;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getEstimationMakerName() {
        return estimationMakerName;
    }

    public void setEstimationMakerName(String estimationMakerName) {
        this.estimationMakerName = estimationMakerName;
    }

    public String getOfferRemarks() {
        return offerRemarks;
    }

    public void setOfferRemarks(String offerRemarks) {
        this.offerRemarks = offerRemarks;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public boolean isCanEditAsAdmin() {
        return canEditAsAdmin;
    }

    public void setCanEditAsAdmin(boolean canEditAsAdmin) {
        this.canEditAsAdmin = canEditAsAdmin;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + id +
            ", internalNumber='" + internalNumber + '\'' +
            ", sapNumber='" + sapNumber + '\'' +
            ", referenceNumber='" + referenceNumber + '\'' +
            ", orderType=" + orderType +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", closeDate=" + closeDate +
            ", orderStatus=" + orderStatus +
            ", clientId=" + clientId +
            ", clientShortcut='" + clientShortcut + '\'' +
            ", inquiryId=" + inquiryId +
            ", estimations=" + estimations +
            ", offerRemarks=" + offerRemarks +
            ", deliveryAddress=" + deliveryAddress +
            ", canEditAsAdmin=" + canEditAsAdmin +
            '}';
    }
}
