package eu.canpack.fip.bo.commercialPart;

import eu.canpack.fip.bo.estimation.EstimationMapper;
import eu.canpack.fip.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity CommercialPart and its DTO CommercialPartDTO.
 */
@Mapper(componentModel = "spring", uses = { EstimationMapper.class, })
public interface CommercialPartMapper extends EntityMapper<CommercialPartDTO, CommercialPart> {



    @Mapping(source = "estimation.id", target = "estimationId")
    CommercialPartDTO toDto(CommercialPart commercialPart);


    @Mapping(source = "estimationId", target = "estimation")
    CommercialPart toEntity(CommercialPartDTO commercialPartDTO);
    default CommercialPart fromId(Long id) {
        if (id == null) {
            return null;
        }
        CommercialPart commercialPart = new CommercialPart();
        commercialPart.setId(id);
        return commercialPart;
    }
}
