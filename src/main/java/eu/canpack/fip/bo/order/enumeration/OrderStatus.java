package eu.canpack.fip.bo.order.enumeration;

/**
 * The OrderStatus enumeration.
 */
public enum OrderStatus {
    WORKING_COPY("KOPIA_ROBOCZA"),
    SENT_TO_ESTIMATION("PRZEKAZANE DO WYCENY"), IN_ESTIMATION("W TRAKCIE WYCENY"),
    SENT_OFFER_TO_CLIENT("OFERTA UKOŃCZONA"), /*CREATED_PURCHASE_ORDER("ZAMÓWIENIE UTWORZONE"),*/
    CREATING_SAP_ORDER("DODAWANE ZLECENIE SAP"), TECHNOLOGY_VERIFICATION("WERYFIKACJA TECHNOLOGII"),
    IN_PRODUCTION("W PRODUKCJI"), FINISHED("ZAKOŃCZONO"),
    TECHNOLOGY_CREATION("TWORZENIE TECHNOLOGII");

    private String plName;

    OrderStatus(String plName) {
        this.plName = plName;

    }

    public String getPlName() {
        return plName;
    }
}
