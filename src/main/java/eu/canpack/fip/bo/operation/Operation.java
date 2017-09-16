package eu.canpack.fip.bo.operation;

import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.machine.Machine;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
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

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "remark")
    private String remark;

    @Column(name = "estimated_time", precision=10, scale=4)
    private BigDecimal estimatedTime;

    @Column(name = "real_time", precision=10, scale=4)
    private BigDecimal realTime;

    @ManyToOne
    private Machine machine;

    @ManyToOne
    private Estimation estimation;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Operation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public Operation remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getEstimatedTime() {
        return estimatedTime;
    }

    public Operation estimatedTime(BigDecimal estimatedTime) {
        this.estimatedTime = estimatedTime;
        return this;
    }

    public void setEstimatedTime(BigDecimal estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public BigDecimal getRealTime() {
        return realTime;
    }

    public Operation realTime(BigDecimal realTime) {
        this.realTime = realTime;
        return this;
    }

    public void setRealTime(BigDecimal realTime) {
        this.realTime = realTime;
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

    public Operation estimation(Estimation estimation) {
        this.estimation = estimation;
        return this;
    }

    public void setEstimation(Estimation estimation) {
        this.estimation = estimation;
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
            "}";
    }
}