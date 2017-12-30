package eu.canpack.fip.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Cooperation.
 */
@Entity
@Table(name = "cooperation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cooperation")
public class Cooperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 512)
    @Column(name = "name", length = 512, nullable = false)
    private String name;

    @NotNull
    @Size(max = 256)
    @Column(name = "counterparty", length = 256, nullable = false)
    private String counterparty;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    @ManyToOne
    private Estimation estimation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Cooperation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public Cooperation counterparty(String counterparty) {
        this.counterparty = counterparty;
        return this;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Cooperation amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Cooperation price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Estimation getEstimation() {
        return estimation;
    }

    public Cooperation estimation(Estimation estimation) {
        this.estimation = estimation;
        return this;
    }

    public void setEstimation(Estimation estimation) {
        this.estimation = estimation;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cooperation cooperation = (Cooperation) o;
        if (cooperation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cooperation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cooperation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", counterparty='" + getCounterparty() + "'" +
            ", amount=" + getAmount() +
            ", price=" + getPrice() +
            "}";
    }
}
