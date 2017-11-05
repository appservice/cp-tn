package eu.canpack.fip.bo.operation.dto;

import eu.canpack.fip.bo.operation.OperationEvent;
import eu.canpack.fip.service.mapper.OperatorMapper;
import org.mapstruct.Mapper;

/**
 * CP S.A.
 * Created by lukasz.mochel on 05.11.2017.
 */
@Mapper(componentModel = "spring", uses = {OperationMapper.class, OperatorMapper.class})
public interface OperationEventMapper {



    OperationEventDTO toDto(OperationEvent operationEvent);

//    @Mapping(source = "estimationId", target = "estimation")
//    @Mapping(source = "machine.id", target = "machine")
    OperationEvent toEntity(OperationEventDTO operationDTO);

    default OperationEvent fromId(Long id) {
        if (id == null) {
            return null;
        }
        OperationEvent operationEvent = new OperationEvent();
        operationEvent.setId(id);
        return operationEvent;
    }


}
