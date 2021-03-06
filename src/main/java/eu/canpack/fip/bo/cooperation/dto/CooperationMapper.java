package eu.canpack.fip.bo.cooperation.dto;

import eu.canpack.fip.bo.cooperation.Cooperation;
import eu.canpack.fip.bo.estimation.EstimationMapper;
import eu.canpack.fip.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Cooperation and its DTO CooperationDTO.
 */
@Mapper(componentModel = "spring", uses = {EstimationMapper.class})
public interface CooperationMapper extends EntityMapper<CooperationDTO, Cooperation> {

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
