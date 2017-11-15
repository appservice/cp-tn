package eu.canpack.fip.bo.order.enumeration;

/**
 * The OrderStatus enumeration.
 */
public enum OrderStatus {
    WORKING_COPY,
    SENT_TO_ESTIMATION, IN_ESTIMATION, SENT_OFFER_TO_CLIENT, CREATED_PURCHASE_ORDER,
    CREATING_SAP_ORDER, TECHNOLOGY_VERIFICATION,
    IN_PRODUCTION, FINISHED,
    TECHNOLOGY_CREATION
}
