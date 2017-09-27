package eu.canpack.fip.bo.client;

import eu.canpack.fip.domain.User;
import eu.canpack.fip.repository.search.ClientSearchRepository;
import eu.canpack.fip.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Client.
 */
@Service
@Transactional
public class ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;


    private final ClientMapper clientMapper;

    private final ClientSearchRepository clientSearchRepository;

    private final UserService userService;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper, ClientSearchRepository clientSearchRepository, UserService userService) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.clientSearchRepository = clientSearchRepository;
        this.userService = userService;
    }

    /**
     * Save a client.
     *
     * @param clientDTO the entity to save
     * @return the persisted entity
     */
    public ClientDTO save(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        ClientDTO result = clientMapper.toDto(client);
        clientSearchRepository.save(client);
        return result;
    }

    /**
     * Get all the clients.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clients");
        return clientRepository.findAll(pageable)
            .map(clientMapper::toDto);
    }

    /**
     * Get all the clients.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllToTypeahead(Pageable pageable) {
        User loggedUser = userService.getLoggedUser();
        if (loggedUser.getClient() != null) {
            List<ClientDTO> clientDTOS = new ArrayList<>();
            ClientDTO clientDTO = clientMapper.toDto(loggedUser.getClient());
            clientDTOS.add(clientDTO);
            return new PageImpl<ClientDTO>(clientDTOS, pageable, 1);

        }
        log.debug("Request to get all Clients");
        return clientRepository.findAll(pageable)
            .map(clientMapper::toDto);
    }

    /**
     * Get one client by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ClientDTO findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        Client client = clientRepository.findOne(id);
        return clientMapper.toDto(client);
    }

    /**
     * Delete the  client by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.delete(id);
        clientSearchRepository.delete(id);
    }

    /**
     * Search for the client corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClientDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Clients for query {}", query);
        Page<Client> result = clientSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(clientMapper::toDto);
    }
}
