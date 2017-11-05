package eu.canpack.fip.bo.operation;

import eu.canpack.fip.bo.operation.dto.OperationDTO;
import eu.canpack.fip.bo.operation.dto.OperationMapper;
import eu.canpack.fip.bo.operation.dto.OperationWideDTO;
import eu.canpack.fip.bo.operation.dto.OperationWideMapper;
import eu.canpack.fip.repository.search.OperationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Operation.
 */
@Service
@Transactional
public class OperationService {

    private final Logger log = LoggerFactory.getLogger(OperationService.class);

    private final OperationRepository operationRepository;

    private final OperationMapper operationMapper;

    private final OperationSearchRepository operationSearchRepository;

    private final OperationWideMapper operationWideMapper;

    public OperationService(OperationRepository operationRepository, OperationMapper operationMapper, OperationSearchRepository operationSearchRepository, OperationWideMapper operationWideMapper) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
        this.operationSearchRepository = operationSearchRepository;
        this.operationWideMapper = operationWideMapper;
    }

    /**
     * Save a operation.
     *
     * @param operationDTO the entity to save
     * @return the persisted entity
     */
    public OperationDTO save(OperationDTO operationDTO) {
        log.debug("Request to save Operation : {}", operationDTO);
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        OperationDTO result = operationMapper.toDto(operation);
        operationSearchRepository.save(operation);
        return result;
    }

    /**
     * Get all the operations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OperationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Operations");
        return operationRepository.findAll(pageable)
            .map(operationMapper::toDto);
    }

    /**
     * Get one operation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OperationDTO findOne(Long id) {
        log.debug("Request to get Operation : {}", id);
        Operation operation = operationRepository.findOne(id);
        return operationMapper.toDto(operation);
    }

    /**
     * Get one operation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OperationWideDTO findOneWide(Long id) {
        log.debug("Request findOneWide to get Operation with wide dto : {}", id);
        Operation operation = operationRepository.findOne(id);
        OperationWideDTO operationWideDTO = operationWideMapper.toDto(operation);
        log.debug("operationWideDTO: {}", operationWideDTO);
        return operationWideDTO;
    }

    /**
     * Delete the  operation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Operation : {}", id);
        operationRepository.delete(id);
        operationSearchRepository.delete(id);
    }

    /**
     * Search for the operation corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OperationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Operations for query {}", query);
        Page<Operation> result = operationSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(operationMapper::toDto);
    }
}
