package eu.canpack.fip.bo.attachment;

import eu.canpack.fip.bo.client.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CP S.A.
 * Created by lukasz.mochel on 23.09.2017.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AttachmentCloneMapper {


    @Mapping(target = "id", ignore = true)
    Client cloneEntity(Client operation);
}
