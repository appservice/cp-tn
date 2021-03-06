package eu.canpack.fip.bo.client;

import eu.canpack.fip.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity Client and its DTO ClientDTO.
 */
@Mapper(componentModel = "spring", uses = {})

public interface ClientMapper extends EntityMapper<ClientDTO, Client> {

    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "users", ignore = true)
    Client toEntity(ClientDTO clientDTO);

    default Client fromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
