package eu.canpack.fip.bo.cooperation;

import eu.canpack.fip.bo.cooperation.dto.CooperationDTO;
import eu.canpack.fip.bo.cooperation.dto.CooperationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Cooperation.
 */
@Service
@Transactional
public class CooperationService {

    private final Logger log = LoggerFactory.getLogger(CooperationService.class);

    private final CooperationRepository cooperationRepository;

    private final CooperationMapper cooperationMapper;


    public CooperationService(CooperationRepository cooperationRepository, CooperationMapper cooperationMapper) {
        this.cooperationRepository = cooperationRepository;
        this.cooperationMapper = cooperationMapper;
    }

    /**
     * Save a cooperation.
     *
     * @param cooperationDTO the entity to save
     * @return the persisted entity
     */
    public CooperationDTO save(CooperationDTO cooperationDTO) {
        log.debug("Request to save Cooperation : {}", cooperationDTO);
        Cooperation cooperation = cooperationMapper.toEntity(cooperationDTO);
        cooperation = cooperationRepository.save(cooperation);
        CooperationDTO result = cooperationMapper.toDto(cooperation);
        return result;
    }

    /**
     * Get all the cooperation.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CooperationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cooperation");
        return cooperationRepository.findAll(pageable)
            .map(cooperationMapper::toDto);
    }

    /**
     * Get one cooperation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CooperationDTO findOne(Long id) {
        log.debug("Request to get Cooperation : {}", id);
        Cooperation cooperation = cooperationRepository.findOne(id);
        return cooperationMapper.toDto(cooperation);
    }

    /**
     * Delete the cooperation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cooperation : {}", id);
        cooperationRepository.delete(id);
    }


}
