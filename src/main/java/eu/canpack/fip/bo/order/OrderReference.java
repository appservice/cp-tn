package eu.canpack.fip.bo.order;

import java.io.Serializable;

/**
 * CP S.A.
 * Created by lukasz.mochel on 03.12.2017.
 */
public class OrderReference implements Serializable {
    private Long id;
    private String internalNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    @Override
    public String toString() {
        return "OrderReference{" +
            "id=" + id +
            ", internalNumber='" + internalNumber + '\'' +
            '}';
    }
}
