package eu.canpack.fip.bo.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.domain.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * CP S.A.
 * Created by lukasz.mochel on 24.02.2018.
 */
@Entity
@Table(name = "tn_audit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Document(indexName = "order")
public class Audit implements Serializable{


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "audited_operation",nullable = false)
    private AuditedOperation operation;

    @Column(name = "created_at",nullable = false)
    private ZonedDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by_id")
    private User createdBy;


    @ManyToOne
    @JsonIgnore
    private Order order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuditedOperation getOperation() {
        return operation;
    }

    public void setOperation(AuditedOperation operation) {
        this.operation = operation;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Audit operation(final AuditedOperation operation) {
        this.operation = operation;
        return this;
    }

    public Audit createdAt(final ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Audit createdBy(final User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Audit order(final Order order) {
        this.order = order;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audit audit = (Audit) o;
        return Objects.equals(id, audit.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Audit{" +
            "id=" + id +
            ", operation=" + operation +
            ", createdAt=" + createdAt +
            ", createdBy=" + createdBy +
            ", order=" + order +
            '}';
    }
}
