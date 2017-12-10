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
public class ProductionItemDTO {
    private Long estimationId;

    private String clientName;

    private String itemNumber;

    private String itemName;

    private Integer amount;

    private String orderNumber;

    private Long orderId;

    private OrderType orderType;

    private int productionProgress;

    private String actualProductionPlace;

    private String nextOperationPlace;

    private boolean readyForDispatch;

    private boolean showProductionOrderLink;

    private boolean showOperationsDetail;

    private LocalDate estimatedRealizationDate;

    private ZonedDateTime createdAt;

    private ZonedDateTime productionStartDateTime;


    public ProductionItemDTO() {
    }

    public ProductionItemDTO(Estimation estimation) {
        this.estimationId = estimation.getId();
        this.estimatedRealizationDate=estimation.getEstimatedRealizationDate();
        this.itemName = estimation.getItemName();
        this.itemNumber = estimation.getItemNumber();
        this.amount = estimation.getAmount();
        this.orderNumber = estimation.getOrder().getInternalNumber();
        this.orderId = estimation.getOrder().getId();
        this.orderType = estimation.getOrder().getOrderType();
        this.productionProgress = calculateProductionProgress(estimation);
        this.actualProductionPlace = getActualProductionPlace(estimation);
        this.nextOperationPlace = this.getNextOperationPlace(estimation);
        if(this.productionProgress==100 && SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.TEAM_LEADER)){
            setReadyForDispatch(true);
        }
        this.clientName=estimation.getOrder().getClient().getShortcut();
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MANAGER) || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.TEAM_LEADER)){
            this.showOperationsDetail=true;
        }
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.TECHNOLOGIST) || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SAP_INTRODUCER)){
            this.showProductionOrderLink =true;
        }
        this.createdAt= estimation.getOrder().getCreatedAt();
        this.productionStartDateTime=estimation.getProductionStartDateTime();



    }

    private int calculateProductionProgress(Estimation estimation) {

        int amountOfOperation = estimation.getOperations().size();
        if (amountOfOperation > 0) {

            int amountOfFinishedOperation = (int) estimation.getOperations().stream()
                .filter(o -> o.getOperationStatus() == OperationStatus.FINISHED).count();
            return amountOfFinishedOperation * 100 / amountOfOperation;
        } else
            return 100;

    }

    private String getActualProductionPlace(Estimation estimation) {
        return estimation.getOperations().stream().filter(o -> o.getOperationStatus() == OperationStatus.STARTED).findFirst().map(o -> o.getMachine().getName()).orElse(null);
    }

    private String getNextOperationPlace(Estimation estimation) {
        return estimation.getOperations().stream()
            .sorted(Comparator.comparing(Operation::getSequenceNumber))
            .filter(o -> o.getOperationStatus() != OperationStatus.FINISHED)
            .findFirst().map(o -> o.getMachine().getName()).orElse(null);
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

    public String getActualProductionPlace() {
        return actualProductionPlace;
    }

    public void setActualProductionPlace(String actualProductionPlace) {
        this.actualProductionPlace = actualProductionPlace;
    }

    public String getNextOperationPlace() {
        return nextOperationPlace;
    }

    public void setNextOperationPlace(String nextOperationPlace) {
        this.nextOperationPlace = nextOperationPlace;
    }

    public boolean isReadyForDispatch() {
        return readyForDispatch;
    }

    public void setReadyForDispatch(boolean readyForDispatch) {
        this.readyForDispatch = readyForDispatch;
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


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getProductionStartDateTime() {
        return productionStartDateTime;
    }

    public void setProductionStartDateTime(ZonedDateTime productionStartDateTime) {
        this.productionStartDateTime = productionStartDateTime;
    }

    @Override
    public String toString() {
        return "ProductionItemDTO{" +
            "estimationId=" + estimationId +
            ", clientName='" + clientName + '\'' +
            ", itemNumber='" + itemNumber + '\'' +
            ", itemName='" + itemName + '\'' +
            ", amount=" + amount +
            ", orderNumber='" + orderNumber + '\'' +
            ", orderId=" + orderId +
            ", orderType=" + orderType +
            ", productionProgress=" + productionProgress +
            ", actualProductionPlace='" + actualProductionPlace + '\'' +
            ", nextOperationPlace='" + nextOperationPlace + '\'' +
            ", readyForDispatch=" + readyForDispatch +
            ", showProductionOrderLink=" + showProductionOrderLink +
            ", showOperationsDetail=" + showOperationsDetail +
            ", estimatedRealizationDate=" + estimatedRealizationDate +
            ", createdAt=" + createdAt +
            ", productionStartDateTime=" + productionStartDateTime +
            '}';
    }
}
