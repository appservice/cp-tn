package eu.canpack.fip.bo.machine;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    private String defaultTechnologyDesc;

    private LocalDate validFrom;

    public MachineDTO() {
    }

    public MachineDTO(Machine machine, MachineDtl machineDtl) {

       this.id=machine.getId();
       this.name=machine.getName();
       this.shortcut=machine.getShortcut();
       if(machineDtl!=null){
           this.workingHourPrice=machineDtl.getWorkingHourPrice();
           this.validFrom=machineDtl.getValidFrom();

       }
       this.defaultTechnologyDesc=machine.getDefaultTechnologyDesc();
    }

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

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public String getDefaultTechnologyDesc() {
        return defaultTechnologyDesc;
    }

    public void setDefaultTechnologyDesc(String defaultTechnologyDesc) {
        this.defaultTechnologyDesc = defaultTechnologyDesc;
    }



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
            "id=" + id +
            ", name='" + name + '\'' +
            ", shortcut='" + shortcut + '\'' +
            ", workingHourPrice=" + workingHourPrice +
            ", defaultTechnologyDesc='" + defaultTechnologyDesc + '\'' +
            ", validFrom=" + validFrom +
            '}';
    }
}
