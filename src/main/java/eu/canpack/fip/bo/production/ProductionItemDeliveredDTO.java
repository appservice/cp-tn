package eu.canpack.fip.bo.production;

import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.operation.enumeration.OperationStatus;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import eu.canpack.fip.security.AuthoritiesConstants;
import eu.canpack.fip.security.SecurityUtils;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Comparator;

/**
 * CP S.A.
 * Created by lukasz.mochel on 29.10.2017.
 */
public class ProductionItemDeliveredDTO {
    private Long estimationId;

    private String clientName;

    private String itemNumber;

    private String itemName;

    private Integer amount;

    private String orderNumber;

    private Long orderId;

    private OrderType orderType;

    private int productionProgress;

    private boolean showProductionOrderLink;

    private boolean showOperationsDetail;

    private LocalDate estimatedRealizationDate;

    private String receiver;

    private ZonedDateTime deliveredAt;


    public ProductionItemDeliveredDTO() {
    }

    public ProductionItemDeliveredDTO(Estimation estimation) {
        this.estimationId = estimation.getId();
        this.estimatedRealizationDate=estimation.getEstimatedRealizationDate();
        this.itemName = estimation.getItemName();
        this.itemNumber = estimation.getItemNumber();
        this.amount = estimation.getAmount();
        this.orderNumber = estimation.getOrder().getInternalNumber();
        this.orderId = estimation.getOrder().getId();
        this.orderType = estimation.getOrder().getOrderType();
        this.productionProgress =100;
        this.deliveredAt=estimation.getDeliveredAt();
        this.receiver=estimation.getReceiver();

        this.clientName=estimation.getOrder().getClient().getShortcut();
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MANAGER) || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.TEAM_LEADER)){
            this.showOperationsDetail=true;
        }
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.TECHNOLOGIST) || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SAP_INTRODUCER)){
            this.showProductionOrderLink =true;
        }



    }



    public LocalDate getEstimatedRealizationDate() {
        return estimatedRealizationDate;
    }

    public void setEstimatedRealizationDate(LocalDate estimatedRealizationDate) {
        this.estimatedRealizationDate = estimatedRealizationDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getEstimationId() {
        return estimationId;
    }

    public void setEstimationId(Long estimationId) {
        this.estimationId = estimationId;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public int getProductionProgress() {
        return productionProgress;
    }

    public void setProductionProgress(int productionProgress) {
        this.productionProgress = productionProgress;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean isShowProductionOrderLink() {
        return showProductionOrderLink;
    }

    public void setShowProductionOrderLink(boolean showProductionOrderLink) {
        this.showProductionOrderLink = showProductionOrderLink;
    }

    public boolean isShowOperationsDetail() {
        return showOperationsDetail;
    }

    public void setShowOperationsDetail(boolean showOperationsDetail) {
        this.showOperationsDetail = showOperationsDetail;
    }


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public ZonedDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    @Override
    public String toString() {
        return "ProductionItemDeliveredDTO{" +
            "estimationId=" + estimationId +
            ", clientName='" + clientName + '\'' +
            ", itemNumber='" + itemNumber + '\'' +
            ", itemName='" + itemName + '\'' +
            ", amount=" + amount +
            ", orderNumber='" + orderNumber + '\'' +
            ", orderId=" + orderId +
            ", orderType=" + orderType +
            ", productionProgress=" + productionProgress +
            ", showProductionOrderLink=" + showProductionOrderLink +
            ", showOperationsDetail=" + showOperationsDetail +
            ", estimatedRealizationDate=" + estimatedRealizationDate +
            ", receiver='" + receiver + '\'' +
            ", deliveredAt=" + deliveredAt +
            '}';
    }
}
