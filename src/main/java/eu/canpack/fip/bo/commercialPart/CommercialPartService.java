package eu.canpack.fip.bo.commercialPart;

import eu.canpack.fip.repository.CommercialPartRepository;
import eu.canpack.fip.repository.search.CommercialPartSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CommercialPart.
 */
@Service
@Transactional
public class CommercialPartService {

    private final Logger log = LoggerFactory.getLogger(CommercialPartService.class);

    private final CommercialPartRepository commercialPartRepository;

    private final CommercialPartMapper commercialPartMapper;

    private final CommercialPartSearchRepository commercialPartSearchRepository;

    public CommercialPartService(CommercialPartRepository commercialPartRepository, CommercialPartMapper commercialPartMapper, CommercialPartSearchRepository commercialPartSearchRepository) {
        this.commercialPartRepository = commercialPartRepository;
        this.commercialPartMapper = commercialPartMapper;
        this.commercialPartSearchRepository = commercialPartSearchRepository;
    }

    /**
     * Save a commercialPart.
     *
     * @param commercialPartDTO the entity to save
     * @return the persisted entity
     */
    public CommercialPartDTO save(CommercialPartDTO commercialPartDTO) {
        log.debug("Request to save CommercialPart : {}", commercialPartDTO);
        CommercialPart commercialPart = commercialPartMapper.toEntity(commercialPartDTO);
        commercialPart = commercialPartRepository.save(commercialPart);
        CommercialPartDTO result = commercialPartMapper.toDto(commercialPart);
        commercialPartSearchRepository.save(commercialPart);
        return result;
    }

    /**
     *  Get all the commercialParts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommercialPartDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommercialParts");
        return commercialPartRepository.findAll(pageable)
            .map(commercialPartMapper::toDto);
    }

    /**
     *  Get one commercialPart by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CommercialPartDTO findOne(Long id) {
        log.debug("Request to get CommercialPart : {}", id);
        CommercialPart commercialPart = commercialPartRepository.findOne(id);
        return commercialPartMapper.toDto(commercialPart);
    }

    /**
     *  Delete the  commercialPart by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CommercialPart : {}", id);
        commercialPartRepository.delete(id);
        commercialPartSearchRepository.delete(id);
    }

    /**
     * Search for the commercialPart corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommercialPartDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CommercialParts for query {}", query);
        Page<CommercialPart> result = commercialPartSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(commercialPartMapper::toDto);
    }
}
