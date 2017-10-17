package eu.canpack.fip.bo.operation;

import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.machine.Machine;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Operation.
 */
@Entity
@Table(name = "operation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "operation")
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sequence_number", nullable = false)
    private Integer sequenceNumber;

    //    @Size(max = 1024)
//    @Column(name = "description", length = 1024)
    @Lob
//max 65,535 letters
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "remark")
    private String remark;

    @Column(name = "estimated_time", precision = 10, scale = 4)
    private BigDecimal estimatedTime;

    @Column(name = "real_time", precision = 10, scale = 4)
    private BigDecimal realTime;

    @ManyToOne
    private Machine machine;

    @ManyToOne
    private Estimation estimation;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_status")
    private OperationStatus operationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    private OperationType operationType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Operation description(String description) {
        this.description = description;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Operation remark(String remark) {
        this.remark = remark;
        return this;
    }

    public BigDecimal getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(BigDecimal estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Operation estimatedTime(BigDecimal estimatedTime) {
        this.estimatedTime = estimatedTime;
        return this;
    }

    public BigDecimal getRealTime() {
        return realTime;
    }

    public void setRealTime(BigDecimal realTime) {
        this.realTime = realTime;
    }

    public Operation realTime(BigDecimal realTime) {
        this.realTime = realTime;
        return this;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }


    public Operation machine(Machine machine) {
        this.machine = machine;
        return this;
    }

    public Estimation getEstimation() {
        return estimation;
    }

    public void setEstimation(Estimation estimation) {
        this.estimation = estimation;
    }

    public Operation estimation(Estimation estimation) {
        this.estimation = estimation;
        return this;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Operation sequenceNumber(final Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Operation operation = (Operation) o;
        if (operation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), operation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Operation{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", remark='" + getRemark() + "'" +
            ", estimatedTime='" + getEstimatedTime() + "'" +
            ", realTime='" + getRealTime() + "'" +
            ", sequenceNumber='" + getSequenceNumber() + "'" +
            "}";
    }
}
