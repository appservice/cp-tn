package eu.canpack.fip.bo.mpkBudgetMapper;

import eu.canpack.fip.bo.client.Client;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MpkBudgetMapper.
 */
@Entity
@Table(name = "mpk_budget_mapper")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "mpkbudgetmapper")
public class MpkBudgetMapper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 6)
    @Column(name = "mpk", length = 6, nullable = false, unique = true)
    private String mpk;

    @NotNull
    @Size(min = 7, max = 15)
    @Pattern(regexp = "^[0-9]*$")
    @Column(name = "budget", length = 15, nullable = false)
    private String budget;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "client_id")
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMpk() {
        return mpk;
    }

    public void setMpk(String mpk) {
        this.mpk = mpk;
    }

    public MpkBudgetMapper mpk(String mpk) {
        this.mpk = mpk;
        return this;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public MpkBudgetMapper budget(String budget) {
        this.budget = budget;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MpkBudgetMapper description(String description) {
        this.description = description;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public MpkBudgetMapper client(Client client) {
        this.client = client;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MpkBudgetMapper mpkBudgetMapper = (MpkBudgetMapper) o;
        if (mpkBudgetMapper.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mpkBudgetMapper.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MpkBudgetMapper{" +
            "id=" + getId() +
            ", mpk='" + getMpk() + "'" +
            ", budget='" + getBudget() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
