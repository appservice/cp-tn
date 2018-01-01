package eu.canpack.fip.config;

import java.time.LocalDate;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final LocalDate BIG_BANG_DATE = LocalDate.of(1970, 1, 1);

    public static final LocalDate DATE_UNTIL_NOTICE = LocalDate.of(5000, 1, 1);

    public static final String DEFAULT_LANGUAGE = "pl";

    private Constants() {
    }
}
