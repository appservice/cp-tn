package eu.canpack.fip.bo.mpkBudgetMapper;

import eu.canpack.fip.bo.client.ClientMapper;
import eu.canpack.fip.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity MpkBudgetMapper and its DTO MpkBudgetMapperDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class})
public interface MpkBudgetMapperMapper extends EntityMapper<MpkBudgetMapperDTO, MpkBudgetMapper> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.shortcut", target = "clientShortcut")
    MpkBudgetMapperDTO toDto(MpkBudgetMapper mpkBudgetMapper);

    @Mapping(source = "clientId", target = "client")
    MpkBudgetMapper toEntity(MpkBudgetMapperDTO mpkBudgetMapperDTO);

    default MpkBudgetMapper fromId(Long id) {
        if (id == null) {
            return null;
        }
        MpkBudgetMapper mpkBudgetMapper = new MpkBudgetMapper();
        mpkBudgetMapper.setId(id);
        return mpkBudgetMapper;
    }
}
