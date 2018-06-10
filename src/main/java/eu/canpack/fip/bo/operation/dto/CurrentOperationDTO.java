package eu.canpack.fip.bo.operation.dto;

import eu.canpack.fip.bo.operation.OperationEvent;
import eu.canpack.fip.bo.operator.OperatorDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * CP S.A.
 * Created by lukasz.mochel on 09.06.2018.
 */
public class CurrentOperationDTO implements Serializable {

    private Long operationEventId;

    private OperatorDTO operator;

    private String machineName;

    private String drawingNo;

    private String detailName;

    private String customerName;

    private String orderNumber;

    private Long orderId;

    private ZonedDateTime staredAt;

    private BigDecimal estimatedWorkTime;

    private Long estimationId;

    public CurrentOperationDTO() {
    }

    public CurrentOperationDTO(OperationEvent operationEvent) {
        this.operationEventId = operationEvent.getId();
        if (operationEvent.getOperation() != null && operationEvent.getOperation().getEstimation() != null) {
            if (operationEvent.getOperation().getEstimation().getDrawing() != null) {
                drawingNo = operationEvent.getOperation().getEstimation().getDrawing().getNumber();

            }
            detailName = operationEvent.getOperation().getEstimation().getItemName();
            orderNumber = operationEvent.getOperation().getEstimation().getOrder().getInternalNumber();
            orderId = operationEvent.getOperation().getEstimation().getOrder().getId();
            customerName = operationEvent.getOperation().getEstimation().getOrder().getClient().getShortcut();
            this.estimationId=operationEvent.getOperation().getEstimation().getId();

        }

        OperatorDTO operatorDTO = new OperatorDTO();
        operatorDTO.setFirstName(operationEvent.getOperator().getFirstName());
        operatorDTO.setLastName(operationEvent.getOperator().getLastName());
        operatorDTO.setId(operationEvent.getOperator().getId());
        this.operator = operatorDTO;

        this.machineName = operationEvent.getOperation().getMachine().getName();
        this.staredAt = operationEvent.getCreatedAt();
        this.estimatedWorkTime = operationEvent.getOperation().getEstimatedTime();


    }

    public OperatorDTO getOperator() {
        return operator;
    }

    public void setOperator(OperatorDTO operator) {
        this.operator = operator;
    }

    public Long getEstimationId() {
        return estimationId;
    }

    public void setEstimationId(Long estimationId) {
        this.estimationId = estimationId;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Long getOperationEventId() {
        return operationEventId;
    }

    public void setOperationEventId(Long operationEventId) {
        this.operationEventId = operationEventId;
    }

    public ZonedDateTime getStaredAt() {
        return staredAt;
    }

    public void setStaredAt(ZonedDateTime staredAt) {
        this.staredAt = staredAt;
    }

    public BigDecimal getEstimatedWorkTime() {
        return estimatedWorkTime;
    }

    public void setEstimatedWorkTime(BigDecimal estimatedWorkTime) {
        this.estimatedWorkTime = estimatedWorkTime;
    }

    @Override
    public String toString() {
        return "CurrentOperationDTO{" +
            "operationEventId=" + operationEventId +
            ", operator=" + operator +
            ", machineName='" + machineName + '\'' +
            ", drawingNo='" + drawingNo + '\'' +
            ", detailName='" + detailName + '\'' +
            ", customerName='" + customerName + '\'' +
            ", orderNumber='" + orderNumber + '\'' +
            ", orderId=" + orderId +
            ", staredAt=" + staredAt +
            ", estimatedWorkTime=" + estimatedWorkTime +
            ", estimationId=" + estimationId +
            '}';
    }
}
