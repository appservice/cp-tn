package eu.canpack.fip.bo.operation;

import eu.canpack.fip.bo.estimation.Estimation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CP S.A.
 * Created by lukasz.mochel on 23.09.2017.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OperationCloneMapper {


    @Mapping(target = "id", ignore = true)
    Estimation cloneEntity(Estimation entityToClone);
}
