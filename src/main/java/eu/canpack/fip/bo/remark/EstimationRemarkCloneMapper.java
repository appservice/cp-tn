package eu.canpack.fip.bo.remark;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CP S.A.
 * Created by lukasz.mochel on 23.09.2017.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EstimationRemarkCloneMapper {


    @Mapping(target = "id", ignore = true)
    EstimationRemark cloneEntity(EstimationRemark entityToClone);


}
