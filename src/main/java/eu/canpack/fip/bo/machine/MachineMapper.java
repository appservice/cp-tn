package eu.canpack.fip.bo.machine;

import eu.canpack.fip.bo.operation.dto.OperationMapper;

import eu.canpack.fip.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity Machine and its DTO MachineDTO.
 */
@Mapper(componentModel = "spring", uses = {OperationMapper.class, })
public interface MachineMapper extends EntityMapper<MachineDTO, Machine> {

//    @Mapping(source = "operations.id", target = "operationsId")
    MachineDTO toDto(Machine machine);

//    @Mapping(source = "operationsId", target = "operations")
    Machine toEntity(MachineDTO machineDTO);

    default Machine fromId(Long id) {
        if (id == null) {
            return null;
        }
        Machine machine = new Machine();
        machine.setId(id);
        return machine;
    }
}
