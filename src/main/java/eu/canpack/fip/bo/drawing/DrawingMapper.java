package eu.canpack.fip.bo.drawing;

import eu.canpack.fip.bo.estimation.EstimationMapper;

import eu.canpack.fip.bo.attachment.AttachmentMapper;
import eu.canpack.fip.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity Drawing and its DTO DrawingDTO.
 */
@Mapper(componentModel = "spring", uses = {EstimationMapper.class, AttachmentMapper.class })
public interface DrawingMapper extends EntityMapper<DrawingDTO, Drawing> {

    @Mapping(source = "estimation.id", target = "estimationId")
    DrawingDTO toDto(Drawing drawing);

    @Mapping(source = "estimationId", target = "estimation")
    Drawing toEntity(DrawingDTO drawingDTO);
    default Drawing fromId(Long id) {
        if (id == null) {
            return null;
        }
        Drawing drawing = new Drawing();
        drawing.setId(id);
        return drawing;
    }

}
