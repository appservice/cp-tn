package eu.canpack.fip.bo.machine;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the Machine entity.
 */
public class MachineDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String shortcut;

    private BigDecimal workingHourPrice;

//    private Long operationsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public BigDecimal getWorkingHourPrice() {
        return workingHourPrice;
    }

    public void setWorkingHourPrice(BigDecimal workingHourPrice) {
        this.workingHourPrice = workingHourPrice;
    }

//    public Long getOperationsId() {
//        return operationsId;
//    }
//
//    public void setOperationsId(Long operationId) {
//        this.operationsId = operationId;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MachineDTO machineDTO = (MachineDTO) o;
        if(machineDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), machineDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MachineDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortcut='" + getShortcut() + "'" +
            ", workingHourPrice='" + getWorkingHourPrice() + "'" +
            "}";
    }
}
