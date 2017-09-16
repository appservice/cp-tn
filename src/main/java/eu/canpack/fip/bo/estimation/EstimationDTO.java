package eu.canpack.fip.bo.estimation;


import eu.canpack.fip.bo.drawing.DrawingDTO;
import eu.canpack.fip.bo.operation.OperationDTO;
import eu.canpack.fip.bo.commercialPart.CommercialPartDTO;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Estimation entity.
 */
public class EstimationDTO implements Serializable {

    private Long id;

//    @NotNull
//private String internalNumber;

    private String material;

    @DecimalMin(value = "0")
    private BigDecimal materialPrice;

    @NotNull
    @Min(value = 0)
    private Integer amount;

    private Double mass;

    private String description;

    @DecimalMin(value = "0")
    private BigDecimal estimatedCost;

    @DecimalMin(value = "0")
    private BigDecimal finalCost;

    private Long orderId;

    private String orderInternalNumber;

    private DrawingDTO drawing;

    private List<OperationDTO> operations;

    private List<CommercialPartDTO> commercialParts;

    private LocalDate neededRealizationDate;

    private LocalDate estimatedRealizationDate;

    private BigDecimal discount;

    private ZonedDateTime createdAt;

    private String createdBy;


    public DrawingDTO getDrawing() {
        return drawing;
    }

    public void setDrawing(DrawingDTO drawing) {
        this.drawing = drawing;
    }

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

    public BigDecimal getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public BigDecimal getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(BigDecimal finalCost) {
        this.finalCost = finalCost;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<OperationDTO> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationDTO> operations) {
        this.operations = operations;
    }

    public List<CommercialPartDTO> getCommercialParts() {
        return commercialParts;
    }

    public void setCommercialParts(List<CommercialPartDTO> commercialParts) {
        this.commercialParts = commercialParts;
    }

    public LocalDate getNeededRealizationDate() {
        return neededRealizationDate;
    }

    public void setNeededRealizationDate(LocalDate neededRealizationDate) {
        this.neededRealizationDate = neededRealizationDate;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }


    public String getOrderInternalNumber() {
        return orderInternalNumber;
    }

    public void setOrderInternalNumber(String orderInternalNumber) {
        this.orderInternalNumber = orderInternalNumber;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getEstimatedRealizationDate() {
        return estimatedRealizationDate;
    }

    public void setEstimatedRealizationDate(LocalDate estimatedRealizationDate) {
        this.estimatedRealizationDate = estimatedRealizationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EstimationDTO estimationDTO = (EstimationDTO) o;
        if(estimationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), estimationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EstimationDTO{" +
            "id=" + id +
            ", material='" + material + '\'' +
            ", materialPrice=" + materialPrice +
            ", amount=" + amount +
            ", mass=" + mass +
            ", description='" + description + '\'' +
            ", estimatedCost=" + estimatedCost +
            ", finalCost=" + finalCost +
            ", orderId=" + orderId +
            ", drawing=" + drawing +
            ", operations=" + operations +
            ", commercialParts=" + commercialParts +
            ", neededRealizationDate=" + neededRealizationDate +
            ", estimatedRealizationDate=" + estimatedRealizationDate +
            '}';
    }
}