package eu.canpack.fip.bo.mpkBudgetMapper;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the MpkBudgetMapper entity.
 */
public class MpkBudgetMapperDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 6)
    private String mpk;

    @NotNull
    @Size(min = 7, max = 15)
    @Pattern(regexp = "^[0-9]*$")
    private String budget;

    private String description;

    private Long clientId;

    private String clientShortcut;

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

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientShortcut() {
        return clientShortcut;
    }

    public void setClientShortcut(String clientShortcut) {
        this.clientShortcut = clientShortcut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MpkBudgetMapperDTO mpkBudgetMapperDTO = (MpkBudgetMapperDTO) o;
        if(mpkBudgetMapperDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mpkBudgetMapperDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MpkBudgetMapperDTO{" +
            "id=" + getId() +
            ", mpk='" + getMpk() + "'" +
            ", budget='" + getBudget() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
