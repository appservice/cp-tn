package eu.canpack.fip.bo.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.canpack.fip.bo.operation.enumeration.OperationEventType;
import eu.canpack.fip.bo.operator.Operator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * CP S.A.
 * Created by lukasz.mochel on 22.10.2017.
 */
@Entity
@Table(name = "operation_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OperationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "crated_at")
    private ZonedDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_event_type")
    private OperationEventType operationEventType;

//    private ZonedDateTime stop;

    @ManyToOne
    @JsonIgnore
    private Operator operator;

    @ManyToOne
    @JsonIgnore
    private Operation operation;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperationEvent that = (OperationEvent) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "OperationEvent{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", operationEventType=" + operationEventType +
            ", operator=" + operator +
            ", operation=" + operation +
            '}';
    }
}
