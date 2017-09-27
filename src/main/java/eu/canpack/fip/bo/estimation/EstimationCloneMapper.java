package eu.canpack.fip.bo.estimation;

import eu.canpack.fip.bo.operation.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CP S.A.
 * Created by lukasz.mochel on 23.09.2017.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EstimationCloneMapper {


    @Mapping(target = "id", ignore = true)
    Operation cloneEntity(Operation operation);
}
