package eu.canpack.fip.bo.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.domain.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name="address", length = 1024)
    private String address;

    @Column(name="nip", length = 15)
    private String nip;

    @Column(name = "shortcut")
    private String shortcut;

    @Column(name="annual_order_number", length = 20)
    @Size(max =20 )
    private String annualOrderNumber;

    @Column(name="print_single_pdf_summary_per_order_item",columnDefinition = "BOOLEAN DEFAULT FALSE" )
    private Boolean printSinglePdfSummaryPerOrderItem=false;


    @OneToMany(mappedBy = "client")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "client",cascade = CascadeType.PERSIST)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<User> users = new ArrayList<>();

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Client name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public Client shortcut(String shortcut) {
        this.shortcut = shortcut;
        return this;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public Client orders(Set<Order> orders) {
        this.orders = orders;
        return this;
    }

    public Client addOrders(Order order) {
        this.orders.add(order);
        order.setClient(this);
        return this;
    }

    public Client removeOrders(Order order) {
        this.orders.remove(order);
        order.setClient(null);
        return this;
    }


    public void setPrintSinglePdfSummaryPerOrderItem(Boolean printSinglePdfSummaryPerOrderItem) {
        this.printSinglePdfSummaryPerOrderItem = printSinglePdfSummaryPerOrderItem;
    }

    public String getAnnualOrderNumber() {
        return annualOrderNumber;
    }

    public void setAnnualOrderNumber(String annualOrderNumber) {
        this.annualOrderNumber = annualOrderNumber;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public Boolean getPrintSinglePdfSummaryPerOrderItem() {
        return printSinglePdfSummaryPerOrderItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        if (client.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortcut='" + getShortcut() + "'" +
            ", annualOrderNumber='" + getAnnualOrderNumber() + "'" +
            "}";
    }
}
