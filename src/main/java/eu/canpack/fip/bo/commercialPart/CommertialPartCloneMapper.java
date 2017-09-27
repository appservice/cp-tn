package eu.canpack.fip.bo.commercialPart;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CP S.A.
 * Created by lukasz.mochel on 23.09.2017.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CommertialPartCloneMapper {


    @Mapping(target = "id", ignore = true)
    CommercialPart cloneEntity(CommercialPart operation);
}
