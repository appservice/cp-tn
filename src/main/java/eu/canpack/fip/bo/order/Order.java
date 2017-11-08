package eu.canpack.fip.bo.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.domain.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "internal_number", nullable = false)
    private String internalNumber;

    @Column(name="inuqiry_number")
    private Integer inquiryNumber;

    @Column(name="purchase_order_number")
    private Integer purchaseOrderNumber;

    @Column(name="emergency_order_number")
    private Integer emergencyOrderNumber;

    @Column(name="year")
    private Integer year;

    @Column(name = "sap_number")
    private String sapNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "name")
    private String name;


    @Column(name = "description")
    private String description;

    @Column(name = "close_date")
    private LocalDate closeDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", orphanRemoval = true,cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Estimation> estimations = new ArrayList<>();

    @ManyToOne
    private Client client;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    @ManyToOne
//    @NotNull
    private User estimationMaker;

    @Column(name="created_at",nullable = false)
    @NotNull
    private ZonedDateTime createdAt;

    @Column(name="estimation_finish_date")
    private ZonedDateTime estimationFinsihDate;

    @Column(name="offer_remarks",length = 1024)
    private String offerRemarks;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public Order internalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
        return this;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getSapNumber() {
        return sapNumber;
    }

    public Order sapNumber(String sapNumber) {
        this.sapNumber = sapNumber;
        return this;
    }

    public void setSapNumber(String sapNumber) {
        this.sapNumber = sapNumber;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Order orderType(OrderType orderType) {
        this.orderType = orderType;
        return this;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getName() {
        return name;
    }

    public Order name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Order description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public Order closeDate(LocalDate closeDate) {
        this.closeDate = closeDate;
        return this;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Order orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<Estimation> getEstimations() {
        return estimations;
    }

    public Order estimations(List<Estimation> estimations) {
        this.estimations = estimations;
        return this;
    }

    public Order addEstimations(Estimation estimation) {
        this.estimations.add(estimation);
        estimation.setOrder(this);
        return this;
    }

    public Order removeEstimations(Estimation estimation) {
        this.estimations.remove(estimation);
        estimation.setOrder(null);
        return this;
    }


    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Order referenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    public void setEstimations(List<Estimation> estimations) {
        this.estimations = estimations;
    }

    public Client getClient() {
        return client;
    }

    public Order client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getEstimationMaker() {
        return estimationMaker;
    }

    public void setEstimationMaker(User estimationMaker) {
        this.estimationMaker = estimationMaker;
    }

    public Integer getInquiryNumber() {
        return inquiryNumber;
    }

    public void setInquiryNumber(Integer number) {
        this.inquiryNumber = number;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public ZonedDateTime getEstimationFinsihDate() {
        return estimationFinsihDate;
    }

    public void setEstimationFinsihDate(ZonedDateTime estimationFinsihDate) {
        this.estimationFinsihDate = estimationFinsihDate;
    }

    public Order estimationFinsihDate(final ZonedDateTime estimationFinsihDate) {
        this.estimationFinsihDate = estimationFinsihDate;
        return this;
    }

    public Integer getEmergencyOrderNumber() {
        return emergencyOrderNumber;
    }

    public void setEmergencyOrderNumber(Integer emergencyOrderNumber) {
        this.emergencyOrderNumber = emergencyOrderNumber;
    }

    public Integer getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(Integer purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }


    public String getOfferRemarks() {
        return offerRemarks;
    }

    public void setOfferRemarks(String offerRemarks) {
        this.offerRemarks = offerRemarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        if (order.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }



    public Order createdBy(User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Order createdAt(ZonedDateTime crdatedAt) {
        this.createdAt = crdatedAt;
        return this;
    }

    public Order estimationMaker(User estimationMaker) {
        this.estimationMaker = estimationMaker;
        return this;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", internalNumber='" + internalNumber + '\'' +
            ", inquiryNumber=" + inquiryNumber +
            ", purchaseOrderNumber=" + purchaseOrderNumber +
            ", emergencyOrderNumber=" + emergencyOrderNumber +
            ", year=" + year +
            ", sapNumber='" + sapNumber + '\'' +
            ", orderType=" + orderType +
            ", referenceNumber='" + referenceNumber + '\'' +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", closeDate=" + closeDate +
            ", orderStatus=" + orderStatus +
//            ", estimations=" + estimations +
            ", client=" + client +
            ", createdBy=" + createdBy +
            ", estimationMaker=" + estimationMaker +
            ", createdAt=" + createdAt +
            ", estimationFinsihDate=" + estimationFinsihDate +
            ", offerRemarks='" + offerRemarks + '\'' +
            '}';
    }
}
