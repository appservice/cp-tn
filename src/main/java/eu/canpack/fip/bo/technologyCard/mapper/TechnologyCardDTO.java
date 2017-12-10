package eu.canpack.fip.bo.technologyCard.mapper;


import eu.canpack.fip.bo.commercialPart.CommercialPart;
import eu.canpack.fip.bo.commercialPart.CommercialPartDTO;
import eu.canpack.fip.bo.cooperation.Cooperation;
import eu.canpack.fip.bo.cooperation.dto.CooperationDTO;
import eu.canpack.fip.bo.drawing.dto.DrawingDTO;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.operation.dto.OperationDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
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


    @Size(max = 128)
    private String materialType;

    private Double mass;

    private String description;

    private ZonedDateTime createdAt;

    private Integer amount;

    private DrawingDTO drawing;

    private BigDecimal materialPrice;

    private String drawingNumber;

    private List<OperationDTO> operations;

    private List<CooperationDTO> cooperationList;

    private List<CommercialPartDTO> commercialParts;

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


    public DrawingDTO getDrawing() {
        return drawing;
    }

    public void setDrawing(DrawingDTO drawing) {
        this.drawing = drawing;
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

    public List<OperationDTO> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationDTO> operations) {
        this.operations = operations;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public List<CooperationDTO> getCooperationList() {
        return cooperationList;
    }

    public void setCooperationList(List<CooperationDTO> cooperationList) {
        this.cooperationList = cooperationList;
    }

    public List<CommercialPartDTO> getCommercialParts() {
        return commercialParts;
    }

    public void setCommercialParts(List<CommercialPartDTO> commercialParts) {
        this.commercialParts = commercialParts;
    }

    public BigDecimal getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
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
        return "TechnologyCardDTO{" +
            "id=" + id +
            ", material='" + material + '\'' +
            ", materialType='" + materialType + '\'' +
            ", mass=" + mass +
            ", description='" + description + '\'' +
            ", createdAt=" + createdAt +
            ", amount=" + amount +
            ", drawing=" + drawing +
            ", drawingNumber='" + drawingNumber + '\'' +
            ", operations=" + operations +
            ", cooperationList=" + cooperationList +
            ", commercialParts=" + commercialParts +
            ", createdByName='" + createdByName + '\'' +
            ", createdById=" + createdById +
            '}';
    }
}
