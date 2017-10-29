package eu.canpack.fip.service.mapper;

import eu.canpack.fip.bo.cooperation.Cooperation;
import eu.canpack.fip.bo.cooperation.dto.CooperationDTO;

import eu.canpack.fip.bo.estimation.EstimationMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity Cooperation and its DTO CooperationDTO.
 */
@Mapper(componentModel = "spring", uses = {EstimationMapper.class, })
public interface CooperationMapper extends EntityMapper <CooperationDTO, Cooperation> {

    @Mapping(source = "estimation.id", target = "estimationId")
    CooperationDTO toDto(Cooperation cooperation);

    @Mapping(source = "estimationId", target = "estimation")
    Cooperation toEntity(CooperationDTO cooperationDTO);
    default Cooperation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cooperation cooperation = new Cooperation();
        cooperation.setId(id);
        return cooperation;
    }
}
