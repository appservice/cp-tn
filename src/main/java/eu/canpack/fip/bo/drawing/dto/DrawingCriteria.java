package eu.canpack.fip.bo.drawing.dto;

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
public class DrawingCriteria implements Serializable {
    /**
     * Class for filtering FirstEnum
     */


    private static final long serialVersionUID = 1L;


    private LongFilter id;

//    private ZonedDateTimeFilter createdAt;


    private StringFilter number;
    private StringFilter name;
    private ZonedDateTimeFilter createdAt;

//    private StringFilter createdByLastName;
//
//    private StringFilter createdByFirstName;

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

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }


    public StringFilter getNumber() {
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderCriteria{" +
            (id != null ? "=" + id + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +


            "}";
    }

}
