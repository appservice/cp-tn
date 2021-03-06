package eu.canpack.fip.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String MANAGER = "ROLE_MANAGER";
    public static final String TECHNOLOGIST = "ROLE_TECHNOLOGIST";
    public static final String SAP_INTRODUCER = "ROLE_SAP_INTRODUCER";
    public static final String TEAM_LEADER="ROLE_TEAM_LEADER";
    public static final String SUPERVISIOR = "ROLE_SUPERVISIOR";
    public static final String ORDER_INTRODUCER = "ROLE_ORDER_INTRODUCER";
    public static final String AUDITOR = "ROLE_AUDITOR";

    public static final String ROLE_DEV = "ROLE_DEV";

    private AuthoritiesConstants() {
    }
}
