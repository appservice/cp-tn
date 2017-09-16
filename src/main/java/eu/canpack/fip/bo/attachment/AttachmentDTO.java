package eu.canpack.fip.bo.attachment;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * CP S.A.
 * Created by lukasz.mochel on 28.07.2017.
 */
public class AttachmentDTO {
    private Long id;

    private String path;

    @NotNull
    private String name;

    private String dataContentType;

    private ZonedDateTime uploadDate;

    private Long drawingId;

    public AttachmentDTO() {
    }

    public AttachmentDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.path = attachment.getPath();
        this.name = attachment.getName();
        this.dataContentType = attachment.getDataContentType();
        this.uploadDate = attachment.getUploadDate();
        if(attachment.getDrawing()!=null)
        this.drawingId = attachment.getDrawing().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public ZonedDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    @Override
    public String toString() {
        return "AttachmentDTO{" +
            "id=" + id +
            ", path='" + path + '\'' +
            ", name='" + name + '\'' +
            ", dataContentType='" + dataContentType + '\'' +
            ", uploadDate=" + uploadDate +
            '}';
    }
}
