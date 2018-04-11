package eu.canpack.fip.bo.machine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.canpack.fip.bo.operation.Operation;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Machine.
 */
@Entity
@Table(name = "machine")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Machine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "shortcut", nullable = false)
    private String shortcut;

//    @Column(name = "working_hour_price", precision=10, scale=2)
//    private BigDecimal workingHourPrice;

    @OneToMany(mappedBy = "machine",cascade = CascadeType.ALL,orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<MachineDtl> machineDtls = new ArrayList<>();


    @OneToMany(mappedBy = "machine")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Operation> operations = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Machine name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public Machine shortcut(String shortcut) {
        this.shortcut = shortcut;
        return this;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }
//
//    public BigDecimal getWorkingHourPrice() {
//        return workingHourPrice;
//    }
//
//    public Machine workingHourPrice(BigDecimal workingHourPrice) {
//        this.workingHourPrice = workingHourPrice;
//        return this;
//    }
//
//    public void setWorkingHourPrice(BigDecimal workingHourPrice) {
//        this.workingHourPrice = workingHourPrice;
//    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public Machine operations(List<Operation> operations) {
        this.operations = operations;
        return this;
    }

    public Machine addOperation(Operation operation){
        this.operations.add(operation);
        return this;
    }

    public Machine removeOperation(Operation operation){
        this.operations.remove(operation);
        return this;
    }

    public List<MachineDtl> getMachineDtls() {
        return machineDtls;
    }

    public void setMachineDtls(List<MachineDtl> machineDtls) {
        this.machineDtls = machineDtls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Machine machine = (Machine) o;
        if (machine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), machine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Machine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortcut='" + getShortcut() + "'" +
//            ", workingHourPrice='" + getWorkingHourPrice() + "'" +
            "}";
    }
}
