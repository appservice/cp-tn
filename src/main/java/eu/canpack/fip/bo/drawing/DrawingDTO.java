package eu.canpack.fip.bo.drawing;


import eu.canpack.fip.bo.attachment.AttachmentDTO;
import eu.canpack.fip.bo.estimation.EstimationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Lob;

/**
 * A DTO for the Drawing entity.
 */
public class DrawingDTO implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(DrawingDTO.class);


    private Long id;

    private String number;

    private String name;

//    private List<EstimationDTO> estimations;

//    private Long estimationId;

    private List<AttachmentDTO> attachments = new ArrayList<>();


    public DrawingDTO() {
    }

    public DrawingDTO(Drawing drawing) {
        log.debug("Drawing in DTO: {}", drawing);
        this.id = drawing.getId();
        this.number = drawing.getNumber();
//        this.estimationId = drawing.getEstimation().getId();
        log.debug("size of attachments: {}",drawing.getAttachments().size());
        this.attachments = drawing.getAttachments().stream()
            .map(AttachmentDTO::new).collect(Collectors.toList());
        this.name=drawing.getName();

    }

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

    public List<AttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDTO> attachments) {
        this.attachments = attachments;
    }

//    public List<EstimationDTO> getEstimations() {
//        return estimations;
//    }
//
//    public void setEstimations(List<EstimationDTO> estimations) {
//        this.estimations = estimations;
//    }

    //    public Long getEstimationId() {
//        return estimationId;
//    }
//
//    public void setEstimationId(Long estimationId) {
//        this.estimationId = estimationId;
//    }

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

        DrawingDTO drawingDTO = (DrawingDTO) o;
        if (drawingDTO.getId() == null || getId() == null) {
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
            "}";
    }
}
