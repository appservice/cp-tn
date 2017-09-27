package eu.canpack.fip.bo.client;

import eu.canpack.fip.bo.attachment.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CP S.A.
 * Created by lukasz.mochel on 23.09.2017.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientCloneMapper {


    @Mapping(target = "id", ignore = true)
    Attachment cloneEntity(Attachment operation);
}
