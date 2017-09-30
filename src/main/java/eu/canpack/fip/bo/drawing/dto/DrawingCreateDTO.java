package eu.canpack.fip.bo.drawing.dto;


import eu.canpack.fip.bo.attachment.AttachmentDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Drawing entity.
 */
public class DrawingCreateDTO implements Serializable {

    private Long id;

    private String number;
    private String name;

    private Long estimationId;

    private List<AttachmentDTO> attachments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getEstimationId() {
        return estimationId;
    }

    public void setEstimationId(Long estimationId) {
        this.estimationId = estimationId;
    }

    public List<AttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DrawingCreateDTO drawingDTO = (DrawingCreateDTO) o;
        if(drawingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), drawingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DrawingDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", name='" + getName() + "'" +
            ", estimationId='" + getEstimationId() + "'" +
            "}";
    }
}
