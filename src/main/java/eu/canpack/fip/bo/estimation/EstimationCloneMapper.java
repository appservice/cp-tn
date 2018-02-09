package eu.canpack.fip.bo.estimation;

import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.operation.dto.OperationCloneMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CP S.A.
 * Created by lukasz.mochel on 23.09.2017.
 */
@Mapper(componentModel = "spring", uses = {OperationCloneMapper.class})
public interface EstimationCloneMapper {


    @Mapping(target = "id", ignore = true)
    Estimation cloneEntity(Estimation operation);
}
