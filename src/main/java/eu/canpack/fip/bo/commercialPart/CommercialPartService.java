package eu.canpack.fip.bo.commercialPart;

import eu.canpack.fip.repository.CommercialPartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing CommercialPart.
 */
@Service
@Transactional
public class CommercialPartService {

    private final Logger log = LoggerFactory.getLogger(CommercialPartService.class);

    private final CommercialPartRepository commercialPartRepository;

    private final CommercialPartMapper commercialPartMapper;


    public CommercialPartService(CommercialPartRepository commercialPartRepository, CommercialPartMapper commercialPartMapper) {
        this.commercialPartRepository = commercialPartRepository;
        this.commercialPartMapper = commercialPartMapper;
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
    }

}
