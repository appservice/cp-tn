package eu.canpack.fip.bo.operation.dto;

import eu.canpack.fip.bo.operation.enumeration.OperationEventType;
import eu.canpack.fip.bo.operator.OperatorDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * CP S.A.
 * Created by lukasz.mochel on 05.11.2017.
 */
public class OperationEventDTO implements Serializable {


    private Long id;

    private ZonedDateTime createdAt;

    private OperationEventType operationEventType;

    private OperatorDTO operator;

    private Long operationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OperationEventType getOperationEventType() {
        return operationEventType;
    }

    public void setOperationEventType(OperationEventType operationEventType) {
        this.operationEventType = operationEventType;
    }

    public OperatorDTO getOperator() {
        return operator;
    }

    public void setOperator(OperatorDTO operator) {
        this.operator = operator;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    @Override
    public String toString() {
        return "OperationEventDTO{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", operationEventType=" + operationEventType +
            ", operator=" + operator +
            ", operationId=" + operationId +
            '}';
    }
}
