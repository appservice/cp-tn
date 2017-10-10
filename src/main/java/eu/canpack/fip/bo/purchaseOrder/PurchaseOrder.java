package eu.canpack.fip.bo.purchaseOrder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import eu.canpack.fip.domain.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CP S.A.
 * Created by lukasz.mochel on 10.10.2017.
 */
//@Entity
//@Table(name = "purcahse_order")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Document(indexName = "purchase_order")
public class PurchaseOrder {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @Column(name = "number")
    private Integer number;

    @Column(name = "year")
    private Integer year;

    @Column(name = "sap_number")
    private String sapNumber;

//    @NotNull
//    @Enumerated(EnumType.STRING)
//    @Column(name = "order_type", nullable = false)
//    private OrderType orderType;

    @Column(name = "external_number")
    private String externalNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_order_status")
    private PurchaseOrderStatus purchaseOrderStatus;

    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Estimation> estimations = new ArrayList<>();

    @ManyToOne
    private Client client;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;


    @Column(name = "created_at", nullable = false)
    @NotNull
    private ZonedDateTime createdAt;

    @Column(name = "estimation_finish_date")
    private ZonedDateTime estimationFinsihDate;
}
