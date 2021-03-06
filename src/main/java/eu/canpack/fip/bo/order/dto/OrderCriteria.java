package eu.canpack.fip.bo.order.dto;

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
public class OrderCriteria implements Serializable {
    /**
     * Class for filtering FirstEnum
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {
    }

    public static class OrderTypeFilter extends Filter<OrderType>{

    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private ZonedDateTimeFilter createdAt;

    private StringFilter referenceNumber;

    private StringFilter internalNumber;

    private OrderStatusFilter orderStatus;

    private LongFilter createdById;

    private LongFilter clientId;

    private StringFilter clientName;


    private OrderTypeFilter orderType;

    private StringFilter title;

    private StringFilter drawingNumber;

    private StringFilter creatorName;

//
//    private StringFilter test;
//
//    private LocalDateFilter costaDate;
//
//    private ZonedDateTimeFilter zoneDate;
//
//    private BigDecimalFilter decimalAge;
//
//    private FirstEnumFilter firstEnym;
//
//    private LongFilter userId;
//
//    public EntTestCriteria() {
//    }


    public StringFilter getDrawingNumber() {
        return drawingNumber;
    }

    public void setDrawingNumber(StringFilter drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public StringFilter getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(StringFilter referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public StringFilter getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(StringFilter internalNumber) {
        this.internalNumber = internalNumber;
    }

    public OrderStatusFilter getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusFilter orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public StringFilter getClientName() {
        return clientName;
    }

    public void setClientName(StringFilter clientName) {
        this.clientName = clientName;
    }

    public OrderTypeFilter getOrderType() {
        return orderType;
    }


    public void setOrderType(OrderTypeFilter orderType) {
        this.orderType = orderType;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(StringFilter creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return "OrderCriteria{" +
                (id != null ? "=" + id + ", " : "") +
                (referenceNumber != null ? "referenceNumber=" + referenceNumber + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (createdById != null ? "createdById=" + createdById + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (orderType != null ? "orderType=" + orderType + ", " : "") +
                (orderStatus != null ? "orderStatus=" + orderStatus + ", " : "") +
                (creatorName != null ? "creatorName=" + creatorName + ", " : "") +

            "}";
    }

}
