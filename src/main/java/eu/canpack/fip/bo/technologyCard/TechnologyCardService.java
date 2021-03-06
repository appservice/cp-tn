package eu.canpack.fip.bo.technologyCard;

import eu.canpack.fip.bo.commercialPart.CommercialPart;
import eu.canpack.fip.bo.commercialPart.CommercialPartMapper;
import eu.canpack.fip.bo.cooperation.Cooperation;
import eu.canpack.fip.bo.cooperation.dto.CooperationMapper;
import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.drawing.DrawingRepository;
import eu.canpack.fip.bo.estimation.dto.EstimationDTO;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.operation.dto.OperationMapper;
import eu.canpack.fip.bo.operation.enumeration.OperationType;
import eu.canpack.fip.bo.technologyCard.mapper.TechnologyCardDTO;
import eu.canpack.fip.bo.technologyCard.mapper.TechnologyCardListDTO;
import eu.canpack.fip.bo.technologyCard.mapper.TechnologyCardListMapper;
import eu.canpack.fip.bo.technologyCard.mapper.TechnologyCardMapper;
//import eu.canpack.fip.repository.search.TechnologyCardSearchRepository;
import eu.canpack.fip.service.UserService;
import eu.canpack.fip.web.rest.errors.CustomParameterizedException;
//import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.ZonedDateTime;
import java.util.Comparator;
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

    private final TechnologyCardListMapper technologyCardListMapper;

//    private final TechnologyCardSearchRepository technologyCardSearchRepository;

    private final OperationMapper operationMapper;

    private final CommercialPartMapper commercialPartMapper;

    private final CooperationMapper cooperationMapper;

    private final TechnologyCardMapper technologyCardMapper;

    private final DrawingRepository drawingRepository;

    private final UserService userService;

    public TechnologyCardService(TechnologyCardRepository technologyCardRepository, TechnologyCardListMapper technologyCardListMapper, /*TechnologyCardSearchRepository technologyCardSearchRepository,*/ OperationMapper operationMapper, CommercialPartMapper commercialPartMapper, CooperationMapper cooperationMapper, TechnologyCardMapper technologyCardMapper, DrawingRepository drawingRepository, UserService userService) {
        this.technologyCardRepository = technologyCardRepository;
        this.technologyCardListMapper = technologyCardListMapper;
//        this.technologyCardSearchRepository = technologyCardSearchRepository;
        this.operationMapper = operationMapper;
        this.commercialPartMapper = commercialPartMapper;
        this.cooperationMapper = cooperationMapper;
        this.technologyCardMapper = technologyCardMapper;
        this.drawingRepository = drawingRepository;
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
        log.debug("technologyCard form mapper {}", technologyCard);
        technologyCard.createdAt(ZonedDateTime.now());
        technologyCard.createdBy(userService.getLoggedUser());
        technologyCard = technologyCardRepository.save(technologyCard);


        TechnologyCardDTO result = technologyCardMapper.toDto(technologyCard);
   //     technologyCardSearchRepository.save(technologyCard);
        return result;
    }

    /**
     * Get all the technologyCards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TechnologyCardListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TechnologyCards");
        return technologyCardRepository.findAll(pageable)
            .map(technologyCardListMapper::toDto);
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
        technologyCard.getOperations().sort(Comparator.comparing(Operation::getSequenceNumber));
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
//        technologyCardSearchRepository.delete(id);
    }

//    /**
//     * Search for the technologyCard corresponding to the query.
//     *
//     * @param query    the query of the search
//     * @param pageable the pagination information
//     * @return the list of entities
//     */
//    @Transactional(readOnly = true)
//    public Page<TechnologyCardListDTO> search(String query, Pageable pageable) {
//        log.debug("Request to search for a page of TechnologyCards for query {}", query);
//       QueryStringQueryBuilder queryBuilder= queryStringQuery(query);//.field("");
//        Page<TechnologyCard> result = technologyCardSearchRepository.search(queryBuilder, pageable);
//        return result.map(technologyCardListMapper::toDto);
//    }

    public TechnologyCard createFromEstimation(EstimationDTO estimationDTO) {
        if(estimationDTO.getMaterial()==null){
            throw new CustomParameterizedException("error.materialCanNotBeNull");
        }

        ZonedDateTime now = ZonedDateTime.now();
        TechnologyCard technologyCard = new TechnologyCard()
            .createdAt(now)
            .description(estimationDTO.getDescription())
            .material(estimationDTO.getMaterial())
            .mass(estimationDTO.getMass())
            .amount(estimationDTO.getAmount())
            .materialType(estimationDTO.getMaterialType());
        technologyCard.setMaterialPrice(estimationDTO.getMaterialPrice());

        if (estimationDTO.getDrawing() !=  null && estimationDTO.getDrawing().getId()!=null) {
            Drawing drawing = drawingRepository.findOne(estimationDTO.getDrawing().getId());
            drawing.getTechnologyCards().add(technologyCard);
            technologyCard.setDrawing(drawing);

        } else {
            Drawing drawing = new Drawing();
            drawing.setNumber(estimationDTO.getItemNumber());
            drawing.setName(estimationDTO.getItemName());
            drawing.getTechnologyCards().add(technologyCard);
            drawing.setCreatedAt(now);
            drawingRepository.save(drawing);
            technologyCard.setDrawing(drawing);


        }


        //clear operations
        List<Operation> operations = getOperations(estimationDTO);
        technologyCard.setOperations(operations);

        technologyCard.setCooperationList(getCooperationList(estimationDTO));
        technologyCard.setCommercialParts(getCommertailParts(estimationDTO));


        technologyCard.setCreatedBy(userService.getLoggedUser());
        technologyCard = technologyCardRepository.save(technologyCard);
//        technologyCardSearchRepository.save(technologyCard);
        return technologyCard;

    }

    private List<Operation> getOperations(EstimationDTO estimationDTO) {
        estimationDTO.getOperations().forEach(o -> {
            o.setId(null);
            o.setEstimationId(null);
        });
        List<Operation> operations = operationMapper.toEntity(estimationDTO.getOperations());
        operations.sort(Comparator.comparing(Operation::getSequenceNumber));
        operations.forEach(o->o.setOperationType(OperationType.TECHNOLOGY_CARD));
        return operations;
    }

    private List<CommercialPart> getCommertailParts(EstimationDTO estimationDTO) {
        estimationDTO.getCommercialParts().forEach(o -> {
            o.setId(null);
            o.setEstimationId(null);
        });

        List<CommercialPart> commercialParts = commercialPartMapper.toEntity(estimationDTO.getCommercialParts());
        return commercialParts;
    }

    private List<Cooperation> getCooperationList(EstimationDTO estimationDTO) {
        estimationDTO.getCooperationList().forEach(o -> {
            o.setId(null);
            o.setEstimationId(null);
        });

        List<Cooperation> cooperationList = cooperationMapper.toEntity(estimationDTO.getCooperationList());
        return cooperationList;
    }
}
