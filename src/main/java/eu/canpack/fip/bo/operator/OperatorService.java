package eu.canpack.fip.bo.operator;

import eu.canpack.fip.repository.OperatorRepository;
import eu.canpack.fip.repository.search.OperatorSearchRepository;
import eu.canpack.fip.service.mapper.OperatorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Operator.
 */
@Service
@Transactional
public class OperatorService {

    private final Logger log = LoggerFactory.getLogger(OperatorService.class);

    private final OperatorRepository operatorRepository;

    private final OperatorMapper operatorMapper;

    private final OperatorSearchRepository operatorSearchRepository;

    public OperatorService(OperatorRepository operatorRepository, OperatorMapper operatorMapper, OperatorSearchRepository operatorSearchRepository) {
        this.operatorRepository = operatorRepository;
        this.operatorMapper = operatorMapper;
        this.operatorSearchRepository = operatorSearchRepository;
    }

    /**
     * Save a operator.
     *
     * @param operatorDTO the entity to save
     * @return the persisted entity
     */
    public OperatorDTO save(OperatorDTO operatorDTO) {
        log.debug("Request to save Operator : {}", operatorDTO);
        Operator operator = operatorMapper.toEntity(operatorDTO);
        operator = operatorRepository.save(operator);
        OperatorDTO result = operatorMapper.toDto(operator);
        operatorSearchRepository.save(operator);
        return result;
    }

    /**
     * Get all the operators.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OperatorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Operators");
        return operatorRepository.findAll(pageable)
            .map(operatorMapper::toDto);
    }

    /**
     * Get one operator by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OperatorDTO findOne(Long id) {
        log.debug("Request to get Operator : {}", id);
        Operator operator = operatorRepository.findOne(id);
        return operatorMapper.toDto(operator);
    }

    /**
     * Delete the operator by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Operator : {}", id);
        operatorRepository.delete(id);
        operatorSearchRepository.delete(id);
    }

    /**
     * Search for the operator corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OperatorDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Operators for query {}", query);
        Page<Operator> result = operatorSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(operatorMapper::toDto);
    }
}
