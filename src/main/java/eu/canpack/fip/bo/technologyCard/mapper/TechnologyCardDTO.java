package eu.canpack.fip.bo.technologyCard.mapper;


import eu.canpack.fip.bo.drawing.dto.DrawingDTO;
import eu.canpack.fip.bo.operation.Operation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the TechnologyCard entity.
 */
public class TechnologyCardDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 256)
    private String material;

    private Double mass;

    private String description;

    @NotNull
    private ZonedDateTime createdAt;

    private Integer amount;

    private DrawingDTO drawing;

    private String drawingNumber;

    private List<Operation> operations;

    private String createdByName;

    private Long createdById;

    public TechnologyCardDTO() {
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }


    public String getDrawingNumber() {
        return drawingNumber;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public DrawingDTO getDrawing() {
        return drawing;
    }

    public void setDrawing(DrawingDTO drawing) {
        this.drawing = drawing;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TechnologyCardDTO technologyCardListDTO = (TechnologyCardDTO) o;
        if(technologyCardListDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), technologyCardListDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TechnologyCardListDTO{" +
            "id=" + getId() +
            ", material='" + getMaterial() + "'" +
            ", mass='" + getMass() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
