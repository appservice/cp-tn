package eu.canpack.fip.bo.attachment;

import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Drawing and its DTO DrawingDTO.
 */
@Mapper(componentModel = "spring", uses = { })
public interface AttachmentMapper extends EntityMapper<AttachmentDTO, Attachment> {

    @Mapping(source = "drawing.id", target = "drawingId")
    AttachmentDTO toDto(Attachment attachment);

    @Mapping(source = "drawingId", target = "drawing")
    Attachment toEntity(AttachmentDTO attachmentDTO);

    default Drawing fromId(Long id) {
        if (id == null) {
            return null;
        }
        Drawing drawing = new Drawing();
        drawing.setId(id);
        return drawing;
    }
}
