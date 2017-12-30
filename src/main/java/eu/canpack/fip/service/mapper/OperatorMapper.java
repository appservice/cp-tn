package eu.canpack.fip.service.mapper;

import eu.canpack.fip.domain.*;
import eu.canpack.fip.service.dto.OperatorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Operator and its DTO OperatorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OperatorMapper extends EntityMapper <OperatorDTO, Operator> {
    
    
    default Operator fromId(Long id) {
        if (id == null) {
            return null;
        }
        Operator operator = new Operator();
        operator.setId(id);
        return operator;
    }
}
