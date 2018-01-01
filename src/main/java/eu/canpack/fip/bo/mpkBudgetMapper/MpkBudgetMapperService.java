package eu.canpack.fip.bo.mpkBudgetMapper;


import eu.canpack.fip.repository.search.MpkBudgetMapperSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MpkBudgetMapper.
 */
@Service
@Transactional
public class MpkBudgetMapperService {

    private final Logger log = LoggerFactory.getLogger(MpkBudgetMapperService.class);

    private final MpkBudgetMapperRepository mpkBudgetMapperRepository;

    private final MpkBudgetMapperMapper mpkBudgetMapperMapper;

    private final MpkBudgetMapperSearchRepository mpkBudgetMapperSearchRepository;

    public MpkBudgetMapperService(MpkBudgetMapperRepository mpkBudgetMapperRepository, MpkBudgetMapperMapper mpkBudgetMapperMapper, MpkBudgetMapperSearchRepository mpkBudgetMapperSearchRepository) {
        this.mpkBudgetMapperRepository = mpkBudgetMapperRepository;
        this.mpkBudgetMapperMapper = mpkBudgetMapperMapper;
        this.mpkBudgetMapperSearchRepository = mpkBudgetMapperSearchRepository;
    }

    /**
     * Save a mpkBudgetMapper.
     *
     * @param mpkBudgetMapperDTO the entity to save
     * @return the persisted entity
     */
    public MpkBudgetMapperDTO save(MpkBudgetMapperDTO mpkBudgetMapperDTO) {
        log.debug("Request to save MpkBudgetMapper : {}", mpkBudgetMapperDTO);
        MpkBudgetMapper mpkBudgetMapper = mpkBudgetMapperMapper.toEntity(mpkBudgetMapperDTO);
        mpkBudgetMapper = mpkBudgetMapperRepository.save(mpkBudgetMapper);
        MpkBudgetMapperDTO result = mpkBudgetMapperMapper.toDto(mpkBudgetMapper);
        mpkBudgetMapperSearchRepository.save(mpkBudgetMapper);
        return result;
    }

    /**
     * Get all the mpkBudgetMappers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MpkBudgetMapperDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MpkBudgetMappers");
        return mpkBudgetMapperRepository.findAll(pageable)
            .map(mpkBudgetMapperMapper::toDto);
    }

    /**
     * Get one mpkBudgetMapper by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MpkBudgetMapperDTO findOne(Long id) {
        log.debug("Request to get MpkBudgetMapper : {}", id);
        MpkBudgetMapper mpkBudgetMapper = mpkBudgetMapperRepository.findOne(id);
        return mpkBudgetMapperMapper.toDto(mpkBudgetMapper);
    }

    /**
     * Delete the  mpkBudgetMapper by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MpkBudgetMapper : {}", id);
        mpkBudgetMapperRepository.delete(id);
        mpkBudgetMapperSearchRepository.delete(id);
    }

    /**
     * Search for the mpkBudgetMapper corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MpkBudgetMapperDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MpkBudgetMappers for query {}", query);
        Page<MpkBudgetMapper> result = mpkBudgetMapperSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(mpkBudgetMapperMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<MpkBudgetMapper> findOneByMpkAndClientId(String mpk, Long clientId) {
        String mpkCleared = mpk.toUpperCase().replace(" ", "");
        return mpkBudgetMapperRepository.findOneByMpkIgnoreCaseAndClientId(mpkCleared, clientId);

    }
}
