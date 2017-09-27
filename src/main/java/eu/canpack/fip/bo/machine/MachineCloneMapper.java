package eu.canpack.fip.bo.machine;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CP S.A.
 * Created by lukasz.mochel on 23.09.2017.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MachineCloneMapper {


    @Mapping(target = "id", ignore = true)
    Machine cloneEntity(Machine entityToClone);
}
