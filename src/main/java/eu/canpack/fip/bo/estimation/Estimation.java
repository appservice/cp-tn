package eu.canpack.fip.bo.estimation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.commercialPart.CommercialPart;
import eu.canpack.fip.domain.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A Estimation.
 */
@Entity
@Table(name = "estimation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "estimation")
public class Estimation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "material")
    private String material;

    @DecimalMin(value = "0")
    @Column(name = "material_price", precision = 10, scale = 2)
    private BigDecimal materialPrice;

    @NotNull
    @Min(value = 0)
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "mass")
    private Double mass;


    @Lob
    //max 65,535 letters
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @DecimalMin(value = "0")
    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost;

    @DecimalMin(value = "0")
    @Column(name = "final_cost", precision = 10, scale = 2)
    private BigDecimal finalCost;

    @OneToOne(mappedBy = "estimation", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Drawing drawing;

    @OneToMany(mappedBy = "estimation", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Operation> operations = new ArrayList<>();

    @ManyToOne(optional = false)
    @NotNull
    private Order order;

    @Column(name = "needed_realization_date")
    private LocalDate neededRealizationDate;

    @Column(name="estimated_realization_date")
    private LocalDate estimatedRealizationDate;


    @OneToMany(mappedBy = "estimation", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<CommercialPart> commercialParts = new ArrayList<>();


    @Column(name = "discount", precision = 8, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @ManyToOne
    private User createdBy;

    @Column(name = "createdAt")
    private ZonedDateTime createdAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Estimation material(String material) {
        this.material = material;
        return this;
    }

    public BigDecimal getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
    }

    public Estimation materialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
        return this;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Estimation amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public Estimation mass(Double mass) {
        this.mass = mass;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Estimation description(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public Estimation estimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
        return this;
    }

    public BigDecimal getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(BigDecimal finalCost) {
        this.finalCost = finalCost;
    }

    public Estimation finalCost(BigDecimal finalCost) {
        this.finalCost = finalCost;
        return this;
    }

    public Drawing getDrawing() {
        return drawing;
    }

    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }

    public Estimation drawing(Drawing drawing) {
        this.drawing = drawing;
        return this;
    }


    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public Estimation operations(List<Operation> operations) {
        this.operations = operations;
        return this;
    }

    public Estimation addOperations(Operation operation) {
        this.operations.add(operation);
        operation.setEstimation(this);
        return this;
    }

    public Estimation removeOperations(Operation operation) {
        this.operations.remove(operation);
        operation.setEstimation(null);
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Estimation order(Order order) {
        this.order = order;
        return this;
    }

    public LocalDate getNeededRealizationDate() {
        return neededRealizationDate;
    }

    public void setNeededRealizationDate(LocalDate neededRealizationDate) {
        this.neededRealizationDate = neededRealizationDate;
    }


    public Estimation neededRealizationDate(LocalDate neededRealizationDate) {
        this.neededRealizationDate = neededRealizationDate;
        return this;
    }

    public List<CommercialPart> getCommercialParts() {
        return commercialParts;
    }

    public void setCommercialParts(List<CommercialPart> commercialParts) {
        this.commercialParts = commercialParts;
    }


    public Estimation commercialParts(List<CommercialPart> commercialParts) {
        this.commercialParts = commercialParts;
        return this;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }


    public Estimation discount(BigDecimal discount) {
        this.discount = discount;
        return this;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public String getCreatorName(){
        if(this.createdBy!=null)
        return this.createdBy.getFirstName()+" "+this.createdBy.getLastName();
        return null;
    }

    public LocalDate getEstimatedRealizationDate() {
        return estimatedRealizationDate;
    }

    public void setEstimatedRealizationDate(LocalDate estimatedRealizationDate) {
        this.estimatedRealizationDate = estimatedRealizationDate;
    }

    public Estimation estimatedRealizationDate(final LocalDate estimatedRealizationDate) {
        this.estimatedRealizationDate = estimatedRealizationDate;
        return this;
    }

    public Estimation createdBy(final User createdBy) {
        this.createdBy = createdBy;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Estimation estimation = (Estimation) o;
        if (estimation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), estimation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Estimation{" +
            "id=" + getId() +
//            ", internalNumber='" + getInternalNumber() + "'" +
            ", material='" + getMaterial() + "'" +
            ", materialPrice='" + getMaterialPrice() + "'" +
            ", amount='" + getAmount() + "'" +
            ", mass='" + getMass() + "'" +
            ", description='" + getDescription() + "'" +
            ", estimatedCost='" + getEstimatedCost() + "'" +
            ", finalCost='" + getFinalCost() + "'" +
            ", neededRealizationDate='" + getNeededRealizationDate() + "'" +
            ", discount='" + getDiscount() + "'" +
            "}";
    }


}