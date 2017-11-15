package eu.canpack.fip.bo.estimation.dto;


import eu.canpack.fip.bo.drawing.dto.DrawingDTO;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.remark.EstimationRemark;
import eu.canpack.fip.bo.remark.EstimationRemarkDTO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A DTO for the Estimation entity.
 */
public class EstimationShowDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer amount;

    private String description;

    private Long orderId;

    private String orderInternalNumber;

    private ZonedDateTime orderCreatedAt;

    private DrawingDTO drawing;

    private String itemNumber;
    private String itemName;

    private LocalDate neededRealizationDate;

    private BigDecimal estimatedCost;
//
//    private boolean checked;

    private LocalDate estimatedRealizationDate;

    private String remark;

    private String clientName;

    private Long clientId;

    private List<EstimationRemarkDTO> estimationRemarks;

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public EstimationShowDTO() {
    }


    public EstimationShowDTO(Estimation estimation) {
        this.id = estimation.getId();
        this.amount = estimation.getAmount();
        this.description = estimation.getDescription();
        this.orderId = estimation.getOrder().getId();
        this.neededRealizationDate = estimation.getNeededRealizationDate();
        if (estimation.getDrawing() != null) {
            this.drawing = new DrawingDTO(estimation.getDrawing());

        }
        this.orderInternalNumber=estimation.getOrder().getInternalNumber();
        this.orderCreatedAt=estimation.getOrder().getCreatedAt();

        this.clientName=estimation.getOrder().getClient().getName();

        this.clientId=estimation.getOrder().getClient().getId();

        this.estimatedCost = estimation.getEstimatedCost();
//        if (this.estimatedCost != null) {
////            setChecked(true);
//        }
        List<EstimationRemark> remarks = estimation.getEstimationRemarks();
        this.estimationRemarks = remarks.stream().map(r -> {
            EstimationRemarkDTO erDTO = new EstimationRemarkDTO();
            erDTO.setCreatedAt(r.getCreatedAt());
            erDTO.setCreatedById(r.getCreatedBy().getId());
            erDTO.setCreatedByName(r.getCreatedBy().getFirstName() + " " + r.getCreatedBy().getLastName());
            erDTO.setId(r.getId());
            erDTO.setEstimationId(r.getEstimation().getId());
            erDTO.setRemark(r.getRemark());
            return erDTO;
        }).collect(Collectors.toList());
        this.estimatedRealizationDate = estimation.getEstimatedRealizationDate();

        this.itemName=estimation.getItemName();
        this.itemNumber=estimation.getItemNumber();
    }

    public EstimationShowDTO(Estimation estimation,Boolean isPricePublised) {
        this(estimation);
        if(!isPricePublised){
            this.setEstimatedCost(null);
        }
    }

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }


    public LocalDate getNeededRealizationDate() {
        return neededRealizationDate;
    }

    public void setNeededRealizationDate(LocalDate neededRealizationDate) {
        this.neededRealizationDate = neededRealizationDate;
    }

    public LocalDate getEstimatedRealizationDate() {
        return estimatedRealizationDate;
    }

    public void setEstimatedRealizationDate(LocalDate estimatedRealizationDate) {
        this.estimatedRealizationDate = estimatedRealizationDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<EstimationRemarkDTO> getEstimationRemarks() {
        return estimationRemarks;
    }

    public void setEstimationRemarks(List<EstimationRemarkDTO> estimationRemarks) {
        this.estimationRemarks = estimationRemarks;
    }


    public String getOrderInternalNumber() {
        return orderInternalNumber;
    }

    public void setOrderInternalNumber(String orderInternalNumber) {
        this.orderInternalNumber = orderInternalNumber;
    }

    public ZonedDateTime getOrderCreatedAt() {
        return orderCreatedAt;
    }

    public void setOrderCreatedAt(ZonedDateTime orderCreatedAt) {
        this.orderCreatedAt = orderCreatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EstimationShowDTO estimationDTO = (EstimationShowDTO) o;
        if (estimationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), estimationDTO.getId());
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EstimationCreateDTO{" +
            "id=" + id +
            ", amount=" + amount +
            ", description='" + description + '\'' +
            ", orderId=" + orderId +
            ", drawing=" + drawing +
            ", itemNumber='" + itemNumber + '\'' +
            ", itemName='" + itemName + '\'' +
            ", neededRealizationDate=" + neededRealizationDate +
            ", estimatedCost=" + estimatedCost +
            ", estimatedRealizationDate=" + estimatedRealizationDate +
            ", remark='" + remark + '\'' +
            ", estimationRemarks=" + estimationRemarks +
            '}';
    }
}
