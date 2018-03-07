package eu.canpack.fip.bo.operation.dto;

import eu.canpack.fip.bo.machine.MachineDTO;
import eu.canpack.fip.bo.operation.OperationEvent;
import eu.canpack.fip.bo.operation.enumeration.OperationStatus;
import eu.canpack.fip.bo.operation.enumeration.OperationType;
import eu.canpack.fip.bo.operation.enumeration.ProductionStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * CP S.A.
 * Created by lukasz.mochel on 05.11.2017.
 */
public class OperationWideDTO implements Serializable {


    private Long id;

    private Integer sequenceNumber;

    private String description;

    private String remark;

    private BigDecimal estimatedTime;

    private BigDecimal realTime;

    private MachineDTO machine;

    private Long estimationId;

    private OperationStatus operationStatus;

    private String material;

    private String materialType;

    private String drawingNumber;


    private String orderInternalNumber;

    private OperationType operationType;

    private ProductionStatus productionStatus;

    private String detailName;

    private List<OperationEventDTO> operationEvents = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(BigDecimal estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public BigDecimal getRealTime() {
        return realTime;
    }

    public void setRealTime(BigDecimal realTime) {
        this.realTime = realTime;
    }

    public MachineDTO getMachine() {
        return machine;
    }

    public void setMachine(MachineDTO machine) {
        this.machine = machine;
    }

    public OperationStatus getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(OperationStatus operationStatus) {
        this.operationStatus = operationStatus;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public ProductionStatus getProductionStatus() {
        return productionStatus;
    }

    public void setProductionStatus(ProductionStatus productionStatus) {
        this.productionStatus = productionStatus;
    }

    public List<OperationEventDTO> getOperationEvents() {
        return operationEvents;
    }

    public void setOperationEvents(List<OperationEventDTO> operationEvents) {
        this.operationEvents = operationEvents;
    }

    public Long getEstimationId() {
        return estimationId;
    }

    public void setEstimationId(Long estimationId) {
        this.estimationId = estimationId;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getDrawingNumber() {
        return drawingNumber;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public String getOrderInternalNumber() {
        return orderInternalNumber;
    }

    public void setOrderInternalNumber(String orderInternalNumber) {
        this.orderInternalNumber = orderInternalNumber;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    @Override
    public String toString() {
        return "OperationWideDTO{" +
            "id=" + id +
            ", sequenceNumber=" + sequenceNumber +
            ", description='" + description + '\'' +
            ", remark='" + remark + '\'' +
            ", estimatedTime=" + estimatedTime +
            ", realTime=" + realTime +
            ", machine=" + machine +
            ", estimationId=" + estimationId +
            ", operationStatus=" + operationStatus +
            ", material='" + material + '\'' +
            ", materialType='" + materialType + '\'' +
            ", drawingNumber='" + drawingNumber + '\'' +
            ", orderInternalNumber='" + orderInternalNumber + '\'' +
            ", operationType=" + operationType +
            ", productionStatus=" + productionStatus +
            ", detailName='" + detailName + '\'' +
            ", operationEvents=" + operationEvents +
            '}';
    }
}
