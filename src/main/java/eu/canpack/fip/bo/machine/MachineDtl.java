package eu.canpack.fip.bo.machine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * CP S.A.
 * Created by lukasz.mochel on 22.10.2017.
 */
@Entity
@Table(name = "machine_dtl")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "machine_dtl")
public class MachineDtl {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "working_hour_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal workingHourPrice;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to",nullable = false)
    private LocalDate validTo;

    @ManyToOne
    @JsonIgnore
    private Machine machine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MachineDtl that = (MachineDtl) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MachineDtl{" +
            "id=" + id +
            ", workingHourPrice=" + workingHourPrice +
            ", validFrom=" + validFrom +
            ", validTo=" + validTo +
            ", machine=" + machine +
            '}';
    }
}
