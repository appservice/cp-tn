package eu.canpack.fip.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Cooperation entity.
 */
public class CooperationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 512)
    private String name;

    @NotNull
    @Size(max = 256)
    private String counterparty;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amount;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    private Long estimationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getEstimationId() {
        return estimationId;
    }

    public void setEstimationId(Long estimationId) {
        this.estimationId = estimationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CooperationDTO cooperationDTO = (CooperationDTO) o;
        if(cooperationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cooperationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CooperationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", counterparty='" + getCounterparty() + "'" +
            ", amount='" + getAmount() + "'" +
            ", price='" + getPrice() + "'" +
            "}";
    }
}
