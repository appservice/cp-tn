package eu.canpack.fip.bo.remark;

import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.domain.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * CP S.A.
 * Created by lukasz.mochel on 17.09.2017.
 */
@Entity
@Table(name = "estimation_remark")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EstimationRemark implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="remark")
    private String remark;

    @Column(name="created_at",nullable = false)
    private ZonedDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User createdBy;

    @ManyToOne
    private Estimation estimation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Estimation getEstimation() {
        return estimation;
    }

    public void setEstimation(Estimation estimation) {
        this.estimation = estimation;
    }

    public String getCreatedByName(){
        return getCreatedBy().getFirstName()+" "+getCreatedBy().getLastName();
    }

    @Override
    public String toString() {
        return "EstimationRemark{" +
            "id=" + id +
            ", remark='" + remark + '\'' +
            ", createdAt=" + createdAt +
            ", createdBy=" + createdBy +
            ", estimation=" + estimation +
            '}';
    }
}
