package eu.canpack.fip.bo.referenceOrder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.canpack.fip.bo.order.Order;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;

/**
 * CP S.A.
 * Created by lukasz.mochel on 03.12.2017.
 */
@Entity
@Table(name = "reference_order")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "referenceOrder")
public class ReferenceOrder implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref_order_id")
    private Long refOrderId;

    @Column(name="ref_internal_number")
    private String refInternalNumber;

    @ManyToOne(optional = false)
    @JsonIgnore
    private Order order;

    public Long getRefOrderId() {
        return refOrderId;
    }

    public void setRefOrderId(Long refOrderId) {
        this.refOrderId = refOrderId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefInternalNumber() {
        return refInternalNumber;
    }

    public void setRefInternalNumber(String internalNumber) {
        this.refInternalNumber = internalNumber;
    }

    @Override
    public String toString() {
        return "ReferenceOrder{" +
            "id=" + id +
            ", refOrderId=" + refOrderId +
            ", refInternalNumber='" + refInternalNumber + '\'' +
            ", order=" + order +
            '}';
    }
}
