package eu.canpack.fip.bo.operation.dto;

import eu.canpack.fip.bo.estimation.EstimationMapper;
import eu.canpack.fip.bo.machine.MachineMapper;
import eu.canpack.fip.bo.operation.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CP S.A.
 * Created by lukasz.mochel on 05.11.2017.
 */
@Mapper(componentModel = "spring", uses = {EstimationMapper.class, MachineMapper.class, OperationEventMapper.class})
public interface OperationWideMapper {


    @Mapping(source = "estimation.id", target = "estimationId")
    OperationWideDTO toDto(Operation operation);

    @Mapping(source = "estimationId", target = "estimation")
    Operation toEntity(OperationWideDTO operationDTO);

    default Operation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Operation operation = new Operation();
        operation.setId(id);
        return operation;
    }


}
