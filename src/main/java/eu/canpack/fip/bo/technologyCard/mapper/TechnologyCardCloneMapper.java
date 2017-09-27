package eu.canpack.fip.bo.technologyCard.mapper;

import eu.canpack.fip.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CP S.A.
 * Created by lukasz.mochel on 23.09.2017.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TechnologyCardCloneMapper {


    @Mapping(target = "id", ignore = true)
    User cloneEntity(User entityToClone);


}
