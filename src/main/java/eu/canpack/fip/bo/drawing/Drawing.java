package eu.canpack.fip.bo.drawing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.attachment.Attachment;
import eu.canpack.fip.bo.technologyCard.TechnologyCard;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A Drawing.
 */
@Entity
@Table(name = "drawing")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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

    @Column(name = "cratedAt")
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy = "drawing")//@ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private List<Estimation> estimations = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.DETACH}, orphanRemoval = false)
    @JoinColumn(name = "drawing_id",referencedColumnName = "id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonManagedReference
    private List<Attachment> attachments = new ArrayList<>();

    @OneToMany(mappedBy = "drawing")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private List<TechnologyCard> technologyCards = new ArrayList<>();



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

    public List<Estimation> getEstimations() {
        return estimations;
    }

    public void setEstimations(List<Estimation> estimations) {
        this.estimations = estimations;
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

    public List<TechnologyCard> getTechnologyCards() {
        return technologyCards;
    }

    public void setTechnologyCards(List<TechnologyCard> technologyCards) {
        this.technologyCards = technologyCards;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
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



    public Drawing number(String number) {
        this.number = number;
        return this;
    }

    public Drawing attachments(List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public Drawing estimations(final List<Estimation> estimations) {
        this.estimations = estimations;
        return this;
    }

    @Override
    public String toString() {
        return "Drawing{" +
            "id=" + id +
            ", number='" + number + '\'' +
            ", name='" + name + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}
