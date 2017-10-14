package eu.canpack.fip.bo.estimation.dto;

import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

import java.io.Serializable;

//import eu.canpack.fip.domain.enumeration.FirstEnum;
//import io.github.jhipster.service.filter.BooleanFilter;
//import io.github.jhipster.service.filter.DoubleFilter;
//import io.github.jhipster.service.filter.FloatFilter;
//import io.github.jhipster.service.filter.IntegerFilter;
//import io.github.jhipster.service.filter.StringFilter;
//import io.github.jhipster.service.filter.BigDecimalFilter;
//
//import io.github.jhipster.service.filter.LocalDateFilter;
//import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the EntTest entity. This class is used in EntTestResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /ent-tests?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EstimationCriteria implements Serializable {
    /**
     * Class for filtering FirstEnum
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {
    }

    public static class OrderTypeFilter extends Filter<OrderType>{

    }

    private static final long serialVersionUID = 1L;

    private String orderInternalNumber;

    private StringFilter itemName;

    private StringFilter itemNumber;

//    private LongFilter createdById;

//    private LongFilter clientId;

    private StringFilter clientName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrderInternalNumber() {
        return orderInternalNumber;
    }

    public void setOrderInternalNumber(String orderInternalNumber) {
        this.orderInternalNumber = orderInternalNumber;
    }

    public StringFilter getItemName() {
        return itemName;
    }

    public void setItemName(StringFilter itemName) {
        this.itemName = itemName;
    }

    public StringFilter getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(StringFilter itemNumber) {
        this.itemNumber = itemNumber;
    }

    public StringFilter getClientName() {
        return clientName;
    }

    public void setClientName(StringFilter clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return "EstimationCriteria{" +
            "orderInternalNumber='" + orderInternalNumber + '\'' +
            ", itemName=" + itemName +
            ", itemNumber='" + itemNumber + '\'' +
            ", clientName=" + clientName +
            '}';
    }
}
