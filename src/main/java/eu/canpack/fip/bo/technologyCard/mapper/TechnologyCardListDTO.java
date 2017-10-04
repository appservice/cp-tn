package eu.canpack.fip.bo.technologyCard.mapper;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TechnologyCard entity.
 */
public class TechnologyCardListDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 256)
    private String material;

    private Double mass;

    private String description;

    @NotNull
    private ZonedDateTime createdAt;

    private String createdByName;

    private String createdById;
    private Integer amount;
    //    private DrawingDTO drawing;
//
    private String drawingNumber;

    private String drawingName;
    private Long drawingId;

    public TechnologyCardListDTO() {
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
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

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    //    public DrawingDTO getDrawing() {
//        return drawing;
//    }
//
//    public void setDrawing(DrawingDTO drawing) {
//        this.drawing = drawing;
//    }


    public String getDrawingName() {
        return drawingName;
    }

    public void setDrawingName(String drawingName) {
        this.drawingName = drawingName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TechnologyCardListDTO technologyCardListDTO = (TechnologyCardListDTO) o;
        if (technologyCardListDTO.getId() == null || getId() == null) {
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
