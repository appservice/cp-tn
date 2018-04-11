package eu.canpack.fip.bo.technologyCard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.canpack.fip.bo.commercialPart.CommercialPart;
import eu.canpack.fip.bo.cooperation.Cooperation;
import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.domain.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A TechnologyCard.
 */
@Entity
@Table(name = "technology_card")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Document(indexName = "technologycard")
public class TechnologyCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "material", length = 256, nullable = false)
    private String material;


    @Size(max = 128)
    @Column(name = "material_type", length = 256, nullable = false)
    private String materialType;


    @DecimalMin(value = "0")
    @Column(name = "material_price", precision = 10, scale = 2)
    private BigDecimal materialPrice;

    @Column(name = "mass")
    private Double mass;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne(optional = false)
    @NotNull
    private Drawing drawing;

    @OneToMany(cascade = CascadeType.ALL)//(mappedBy = "technologyCard")
    @JoinColumn(name = "technology_card_id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Operation> operations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)//(mappedBy = "technologyCard")
    @JoinColumn(name = "technology_card_id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<CommercialPart> commercialParts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)//(mappedBy = "technologyCard")
    @JoinColumn(name = "technology_card_id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Cooperation> cooperationList = new ArrayList<>();

    @ManyToOne
//    @JoinColumn(name = "user_id")
    private User createdBy;


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

    public TechnologyCard material(String material) {
        this.material = material;
        return this;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public TechnologyCard mass(Double mass) {
        this.mass = mass;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TechnologyCard description(String description) {
        this.description = description;
        return this;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public TechnologyCard createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public TechnologyCard amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public Drawing getDrawing() {
        return drawing;
    }

    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }

    public TechnologyCard drawing(Drawing drawing) {
        this.drawing = drawing;
        return this;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public TechnologyCard createdBy(final User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public BigDecimal getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
    }

    //    public TechnologyCard addOperations(Operation operation) {
//        this.operations.add(operation);
//        operation.setTechnologyCard(this);
//        return this;
//    }
//
//    public TechnologyCard removeOperations(Operation operation) {
//        this.operations.remove(operation);
//        operation.setTechnologyCard(null);
//        return this;
//    }



    public TechnologyCard operations(List<Operation> operations) {
        this.operations = operations;
        return this;
    }


    public String getCreatorName(){
        if(this.createdBy!=null)
            return this.createdBy.getFirstName()+" "+this.createdBy.getLastName();
        return null;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public TechnologyCard materialType(final String materialType) {
        this.materialType = materialType;
        return this;
    }


    public List<CommercialPart> getCommercialParts() {
        return commercialParts;
    }

    public void setCommercialParts(List<CommercialPart> commercialParts) {
        this.commercialParts = commercialParts;
    }

    public List<Cooperation> getCooperationList() {
        return cooperationList;
    }

    public void setCooperationList(List<Cooperation> cooperationList) {
        this.cooperationList = cooperationList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TechnologyCard technologyCard = (TechnologyCard) o;
        if (technologyCard.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), technologyCard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TechnologyCard{" +
            "id=" + getId() +
            ", material='" + getMaterial() + "'" +
            ", mass='" + getMass() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
