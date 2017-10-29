package eu.canpack.fip.bo.cooperation.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;





/**
 * Criteria class for the Cooperation entity. This class is used in CooperationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /cooperation?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CooperationCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter counterparty;

    private BigDecimalFilter amount;

    private BigDecimalFilter price;

    private LongFilter estimationId;

    public CooperationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(StringFilter counterparty) {
        this.counterparty = counterparty;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public LongFilter getEstimationId() {
        return estimationId;
    }

    public void setEstimationId(LongFilter estimationId) {
        this.estimationId = estimationId;
    }

    @Override
    public String toString() {
        return "CooperationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (counterparty != null ? "counterparty=" + counterparty + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (estimationId != null ? "estimationId=" + estimationId + ", " : "") +
            "}";
    }

}
