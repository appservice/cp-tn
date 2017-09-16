package eu.canpack.fip.bo.technologyCard;

import eu.canpack.fip.bo.order.enumeration.OrderStatus;
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
public class TechnologyCardCriteria implements Serializable {
    /**
     * Class for filtering FirstEnum
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;


    private StringFilter description;


    private ZonedDateTimeFilter createdAt;

    private LongFilter createdById;

    private LongFilter clientId;

    private StringFilter createdByLastName;

    private StringFilter createdByFirstName;

    private StringFilter drawingName;

    private StringFilter drawingNumber;


    public TechnologyCardCriteria() {
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

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public StringFilter getDrawingNumber() {
        return drawingNumber;
    }

    public void setDrawingNumber(StringFilter drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public StringFilter getCreatedByLastName() {
        return createdByLastName;
    }

    public void setCreatedByLastName(StringFilter createdByLastName) {
        this.createdByLastName = createdByLastName;
    }

    public StringFilter getCreatedByFirstName() {
        return createdByFirstName;
    }

    public void setCreatedByFirstName(StringFilter createdByFirstName) {
        this.createdByFirstName = createdByFirstName;
    }

    public StringFilter getDrawingName() {
        return drawingName;
    }

    public void setDrawingName(StringFilter drawingName) {
        this.drawingName = drawingName;
    }

    @Override
    public String toString() {
        return "TechnologyCardCriteria{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", description=" + description +
            ", createdById=" + createdById +
            ", clientId=" + clientId +
            ", drawingName=" + drawingName +
            ", drawingNumber=" + drawingNumber +
            ", createdByLastName=" + createdByLastName +
            ", createdByFirstName=" + createdByFirstName +
            '}';
    }
}
