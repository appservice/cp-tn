package eu.canpack.fip.bo.technologyCard;

import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.estimation.EstimationDTO;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.operation.OperationMapper;
import eu.canpack.fip.repository.search.TechnologyCardSearchRepository;
import eu.canpack.fip.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.ZonedDateTime;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TechnologyCard.
 */
@Service
@Transactional
public class TechnologyCardService {

    private final Logger log = LoggerFactory.getLogger(TechnologyCardService.class);

    private final TechnologyCardRepository technologyCardRepository;

    private final TechnologyCardMapper technologyCardMapper;

    private final TechnologyCardSearchRepository technologyCardSearchRepository;

    private final OperationMapper operationMapper;

    private final UserService userService;

    public TechnologyCardService(TechnologyCardRepository technologyCardRepository, TechnologyCardMapper technologyCardMapper, TechnologyCardSearchRepository technologyCardSearchRepository, OperationMapper operationMapper, UserService userService) {
        this.technologyCardRepository = technologyCardRepository;
        this.technologyCardMapper = technologyCardMapper;
        this.technologyCardSearchRepository = technologyCardSearchRepository;
        this.operationMapper = operationMapper;
        this.userService = userService;
    }

    /**
     * Save a technologyCard.
     *
     * @param technologyCardDTO the entity to save
     * @return the persisted entity
     */
    public TechnologyCardDTO save(TechnologyCardDTO technologyCardDTO) {
        log.debug("Request to save TechnologyCard : {}", technologyCardDTO);
        TechnologyCard technologyCard = technologyCardMapper.toEntity(technologyCardDTO);
        technologyCard = technologyCardRepository.save(technologyCard);
        TechnologyCardDTO result = technologyCardMapper.toDto(technologyCard);
        technologyCardSearchRepository.save(technologyCard);
        return result;
    }

    /**
     * Get all the technologyCards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TechnologyCardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TechnologyCards");
        return technologyCardRepository.findAll(pageable)
            .map(technologyCardMapper::toDto);
    }

    /**
     * Get one technologyCard by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TechnologyCardDTO findOne(Long id) {
        log.debug("Request to get TechnologyCard : {}", id);
        TechnologyCard technologyCard = technologyCardRepository.findOne(id);
        return technologyCardMapper.toDto(technologyCard);
    }

    /**
     * Delete the  technologyCard by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TechnologyCard : {}", id);
        technologyCardRepository.delete(id);
        technologyCardSearchRepository.delete(id);
    }

    /**
     * Search for the technologyCard corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TechnologyCardDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TechnologyCards for query {}", query);
        Page<TechnologyCard> result = technologyCardSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(technologyCardMapper::toDto);
    }

    public TechnologyCard createFromEstimation(EstimationDTO estimationDTO) {
        TechnologyCard technologyCard = new TechnologyCard()
            .createdAt(ZonedDateTime.now())
            .description(estimationDTO.getDescription())
            .material(estimationDTO.getMaterial())
            .mass(estimationDTO.getMass())
            .amount(estimationDTO.getAmount());
        Drawing drawing = new Drawing();
        drawing.setId(estimationDTO.getDrawing().getId());
        technologyCard.setDrawing(drawing);

        //clear operations
        estimationDTO.getOperations().forEach(o -> {
            o.setId(null);
            o.setEstimationId(null);
        });


        List<Operation> operations = operationMapper.toEntity(estimationDTO.getOperations());
        technologyCard.setOperations(operations);
        technologyCard.setCreatedBy(userService.getLoggedUser());
        return technologyCardRepository.save(technologyCard);

    }
}
