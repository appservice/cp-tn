package eu.canpack.fip.bo.production;

import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.operation.enumeration.OperationStatus;
import eu.canpack.fip.bo.operation.enumeration.ProductionStatus;
import eu.canpack.fip.bo.order.enumeration.OrderType;

import java.util.Comparator;

/**
 * CP S.A.
 * Created by lukasz.mochel on 29.10.2017.
 */
public class ProductionItemDTO {
    private Long estimationId;

    private String itemNumber;

    private String itemName;

    private Integer amount;

    private String orderNumber;

    private Long orderId;

    private OrderType orderType;

    private int productionProgress;

    private String actualProductionPlace;

    private String nextOperationPlace;


    public ProductionItemDTO() {
    }

    public ProductionItemDTO(Estimation estimation) {
        this.estimationId = estimation.getId();
        this.itemName = estimation.getItemName();
        this.itemNumber = estimation.getItemNumber();
        this.amount = estimation.getAmount();
        this.orderNumber = estimation.getOrder().getInternalNumber();
        this.orderId = estimation.getOrder().getId();
        this.orderType = estimation.getOrder().getOrderType();
        this.productionProgress = calculateProductionProgress(estimation);
        this.actualProductionPlace = getActualProductionPlace(estimation);
        this.nextOperationPlace = this.getNextOperationPlace(estimation);


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
            .filter(o -> o.getProductionStatus() != ProductionStatus.FINISHED)
            .findFirst().map(o -> o.getMachine().getName()).orElse(null);
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


    @Override
    public String toString() {
        return "ProductionItemDTO{" +
            "estimationId=" + estimationId +
            ", itemNumber='" + itemNumber + '\'' +
            ", itemName='" + itemName + '\'' +
            ", amount=" + amount +
            ", orderNumber='" + orderNumber + '\'' +
            ", orderType=" + orderType +
            ", productionProgress=" + productionProgress +
            ", actualProductionPlace='" + actualProductionPlace + '\'' +
            '}';
    }
}
