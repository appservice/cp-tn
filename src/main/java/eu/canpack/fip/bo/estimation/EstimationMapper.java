package eu.canpack.fip.bo.estimation;

import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.estimation.dto.EstimationDTO;
import eu.canpack.fip.bo.operation.dto.OperationMapper;
import eu.canpack.fip.bo.order.dto.OrderMapper;

import eu.canpack.fip.bo.drawing.DrawingMapper;
import eu.canpack.fip.bo.commercialPart.CommercialPartMapper;
import eu.canpack.fip.bo.remark.EstimationRemarkMapper;
import eu.canpack.fip.bo.cooperation.dto.CooperationMapper;
import eu.canpack.fip.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity Estimation and its DTO EstimationDTO.
 */
@Mapper(componentModel = "spring", uses = {OrderMapper.class, DrawingMapper.class, OperationMapper.class, CooperationMapper.class, CommercialPartMapper.class, EstimationRemarkMapper.class})
public interface EstimationMapper extends EntityMapper<EstimationDTO, Estimation> {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.internalNumber", target = "orderInternalNumber")
    @Mapping(target = "createdBy",expression = "java( estimation.getCreatorName())")
    @Mapping(target = "remark", ignore = true)
    EstimationDTO toDto(Estimation estimation);

//    @Mapping(target = "operations", ignore = true)
    @Mapping(source = "orderId", target = "order")
    @Mapping(target = "createdBy",ignore = true)
//    @Mapping(target = "drawing", ignore = true)
    @Mapping(target = "deliveredAt", ignore = true)
    Estimation toEntity(EstimationDTO estimationDTO);
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


//    @Mapping(target = "operations", ignore = true)
//    @Mapping(target = "material", ignore = true)
//    @Mapping(target = "materialPrice", ignore = true)
//    @Mapping(target = "amount", ignore = true)
//    @Mapping(target = "mass", ignore = true)
//    @Mapping(target = "estimatedCost", ignore = true)
//    @Mapping(target = "finalCost", ignore = true)
//    @Mapping(source = "orderId", target = "order")
//    Estimation toEntity(EstimationCreateDTO estimationCreateDTO);

//    List<Estimation> toEstimationList(List<EstimationCreateDTO> estimationCreateDTOS);
//    java.util.List<eu.canpack.fip.bo.estimation.Estimation> mapToList(java.util.List<eu.canpack.fip.bo.estimation.dto.EstimationCreateDTO> value);
}
