package eu.canpack.fip.bo.estimation.dto;

import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.drawing.DrawingMapper;
import eu.canpack.fip.bo.estimation.Estimation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Estimation and its DTO EstimationDTO.
 */
@Mapper(componentModel = "spring", uses = {DrawingMapper.class})
public interface EstimationCreateMapper  {

//    @Mapping(source = "order.id", target = "orderId")
//    EstimationDTO toDto(Estimation estimation);

//    @Mapping(target = "operations", ignore = true)
//    @Mapping(source = "orderId", target = "order")
//    Estimation toEntity(EstimationDTO estimationDTO);

    @Mapping(target = "operations", ignore = true)
    @Mapping(target = "material", ignore = true)
    @Mapping(target = "materialPrice", ignore = true)
//    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "mass", ignore = true)
    @Mapping(target = "estimatedCost", ignore = true)
    @Mapping(target = "finalCost", ignore = true)
    @Mapping( target = "order",ignore = true)
    @Mapping( target = "estimationRemarks",ignore = true)
//    @Mapping(target = "drawing", ignore =true)
    Estimation toEntity(EstimationCreateDTO estimationCreateDTO);


    default Estimation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Estimation estimation = new Estimation();
        estimation.setId(id);
        return estimation;
    }
    default Drawing drawingFromId(Long id) {
        if (id == null) {
            return null;
        }
        Drawing drawing = new Drawing();
        drawing.setId(id);
        return drawing;
    }

    List<Estimation> toList(List<EstimationCreateDTO> estimationCreateDTOS);

}
