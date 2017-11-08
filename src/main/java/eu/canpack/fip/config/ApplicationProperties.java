package eu.canpack.fip.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private String drawingDirectoryPath;

    private String initialPassword;

    private String initialOfferRemarks;

    public String getDrawingDirectoryPath() {
        return drawingDirectoryPath;
    }

    public void setDrawingDirectoryPath(String drawingDirectoryPath) {
        this.drawingDirectoryPath = drawingDirectoryPath;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }

    public String getInitialOfferRemarks() {
        return initialOfferRemarks;
    }

    public void setInitialOfferRemarks(String initialOfferRemarks) {
        this.initialOfferRemarks = initialOfferRemarks;
    }
}
