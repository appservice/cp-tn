package eu.canpack.fip.bo.technologyCard.mapper;

import eu.canpack.fip.bo.drawing.DrawingMapper;
import eu.canpack.fip.bo.operation.OperationMapper;
import eu.canpack.fip.bo.technologyCard.TechnologyCard;
import eu.canpack.fip.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity TechnologyCard and its DTO TechnologyCardListDTO.
 */
@Mapper(componentModel = "spring", uses = {DrawingMapper.class, OperationMapper.class,DrawingMapper.class})
public interface TechnologyCardMapper extends EntityMapper<TechnologyCardDTO, TechnologyCard> {


    @Mapping(target = "createdByName",expression = "java(technologyCard.getCreatorName())")
    @Mapping(source = "createdBy.id",target = "createdById")
    TechnologyCardDTO toDto(TechnologyCard technologyCard);

    @Mapping(source = "drawing.id", target = "drawing")
  //  @Mapping(target = "operations", ignore = true)
    TechnologyCard toEntity(TechnologyCardDTO technologyCardDTO);
    default TechnologyCard fromId(Long id) {
        if (id == null) {
            return null;
        }
        TechnologyCard technologyCard = new TechnologyCard();
        technologyCard.setId(id);
        return technologyCard;
    }


}
