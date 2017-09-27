package eu.canpack.fip.bo.remark;

import eu.canpack.fip.bo.estimation.EstimationMapper;
import eu.canpack.fip.service.mapper.EntityMapper;
import eu.canpack.fip.service.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CP S.A.
 * Created by lukasz.mochel on 18.09.2017.
 */
@Mapper(componentModel = "spring",uses = {UserMapper.class, EstimationMapper.class})
public interface EstimationRemarkMapper extends EntityMapper<EstimationRemarkDTO,EstimationRemark>{

    @Mapping(source = "createdBy.id",target = "createdById")
    @Mapping(source = "estimation.id",target = "estimationId")
    @Mapping(target = "createdByName",expression = "java(estimationRemark.getCreatedByName())")
    EstimationRemarkDTO toDto(EstimationRemark estimationRemark);

    @Mapping(source = "createdById",target="createdBy")
    @Mapping(source = "estimationId",target = "estimation")
    EstimationRemark toEntity(EstimationRemarkDTO estimationRemarkDTO);

    default EstimationRemark fromId(Long id){
        if(id==null){
            return null;
        }
        EstimationRemark er = new EstimationRemark();
        er.setId(id);
        return er;
    }

}
