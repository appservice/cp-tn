package eu.canpack.fip.bo.operation.dto;

import eu.canpack.fip.bo.estimation.EstimationMapper;
import eu.canpack.fip.bo.machine.MachineMapper;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Operation and its DTO OperationDTO.
 */
@Mapper(componentModel = "spring", uses = {EstimationMapper.class, MachineMapper.class})
public interface OperationMapper extends EntityMapper<OperationDTO, Operation> {

    @Mapping(source = "estimation.id", target = "estimationId")
//    @Mapping(source = "estimation.id", target = "estimationId")
//    @Mapping(target = "machine", ignore = true)
    OperationDTO toDto(Operation operation);

    @Mapping(source = "estimationId", target = "estimation")
    @Mapping(source = "machine.id", target = "machine")
    @Mapping(target = "operationEvents", ignore = true)
    Operation toEntity(OperationDTO operationDTO);

    default Operation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Operation operation = new Operation();
        operation.setId(id);
        return operation;
    }


}
