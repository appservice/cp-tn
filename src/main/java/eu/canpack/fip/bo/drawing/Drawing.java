package eu.canpack.fip.bo.drawing;

import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.attachment.Attachment;
import eu.canpack.fip.bo.technologyCard.TechnologyCard;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * A Drawing.
 */
@Entity
@Table(name = "drawing")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "drawing")
public class Drawing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "drawing_number", nullable = false)
    private String number;

    @Column(name="name")
    private String name;


    @OneToOne
    private Estimation estimation;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.DETACH}, orphanRemoval = false)
    @JoinColumn(name = "drawing_id",referencedColumnName = "id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Attachment> attachments = new ArrayList<>();

    @OneToMany(mappedBy = "drawing")
    private List<TechnologyCard> technologyCard = new ArrayList<>();

    public Drawing() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Estimation getEstimation() {
        return estimation;
    }

    public void setEstimation(Estimation estimation) {
        this.estimation = estimation;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TechnologyCard> getTechnologyCard() {
        return technologyCard;
    }

    public void setTechnologyCard(List<TechnologyCard> technologyCard) {
        this.technologyCard = technologyCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Drawing drawing = (Drawing) o;
        if (drawing.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), drawing.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Drawing{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +

            "}";
    }

    public Drawing number(String number) {
        this.number = number;
        return this;
    }

    public Drawing estimation(Estimation estimation) {
        this.estimation = estimation;
        return this;
    }

    public Drawing attachments(List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }
}
