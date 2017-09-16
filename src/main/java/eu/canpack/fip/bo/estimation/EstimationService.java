package eu.canpack.fip.bo.estimation;

import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.operation.OperationMapper;
import eu.canpack.fip.bo.commercialPart.CommercialPart;
import eu.canpack.fip.bo.pdf.TechnologyCardPdfCreator;
import eu.canpack.fip.domain.User;
import eu.canpack.fip.repository.UserRepository;
import eu.canpack.fip.repository.search.EstimationSearchRepository;
import eu.canpack.fip.bo.commercialPart.CommercialPartMapper;
import eu.canpack.fip.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Estimation.
 */
@Service
@Transactional
public class EstimationService {

    private final Logger log = LoggerFactory.getLogger(EstimationService.class);

    private final EstimationRepository estimationRepository;

    private final EstimationMapper estimationMapper;

    private final EstimationSearchRepository estimationSearchRepository;

    private final OperationMapper operationMapper;

    private final CommercialPartMapper commercialPartMapper;

    private final UserRepository userRepository;

    private final TechnologyCardPdfCreator technologyCardPdfCreator;

    private final UserService userService;

    public EstimationService(EstimationRepository estimationRepository, EstimationMapper estimationMapper, EstimationSearchRepository estimationSearchRepository, OperationMapper operationMapper, CommercialPartMapper commercialPartMapper, UserRepository userRepository, TechnologyCardPdfCreator technologyCardPdfCreator, UserService userService) {
        this.estimationRepository = estimationRepository;
        this.estimationMapper = estimationMapper;
        this.estimationSearchRepository = estimationSearchRepository;
        this.operationMapper = operationMapper;
        this.commercialPartMapper = commercialPartMapper;
        this.userRepository = userRepository;
        this.technologyCardPdfCreator = technologyCardPdfCreator;
        this.userService = userService;
    }

    /**
     * Save a estimation.
     *
     * @param estimationDTO the entity to save
     * @return the persisted entity
     */
    public EstimationDTO save(EstimationDTO estimationDTO) {
        log.debug("Request to save Estimation : {}", estimationDTO);
        Estimation estimation = estimationMapper.toEntity(estimationDTO);
        List<Operation> operations = operationMapper.toEntity(estimationDTO.getOperations());
        log.debug("operations DTO: {}",estimationDTO.getOperations());
        log.debug("operations list: {}",operations);
        Estimation finalEstimation = estimation;
        operations.forEach(operation -> operation.setEstimation(finalEstimation));
        estimation.setOperations(operations);
        List<CommercialPart> commercialParts = commercialPartMapper.toEntity(estimationDTO.getCommercialParts());
        commercialParts.forEach(cp -> cp.setEstimation(finalEstimation));
        estimation.setCommercialParts(commercialParts);


        estimation.setCreatedAt(ZonedDateTime.now());
        User currentUser=userService.getLoggedUser();
        estimation.setCreatedBy(currentUser);

        estimation = estimationRepository.save(estimation);
        estimationSearchRepository.save(estimation);
        EstimationDTO result = estimationMapper.toDto(estimation);

        return result;
    }

//    /**
//     * Save a estimation.
//     *
//     * @param estimationDTO the entity to save
//     * @return the persisted entity
//     */
//    public EstimationDTO save(EstimationCreateNewDTO estimationDTO) {
//        log.debug("Request to save Estimation : {}", estimationDTO);
//        Estimation estimation = estimationMapper.toEntity(estimationDTO);
//        estimation = estimationRepository.save(estimation);
//        EstimationDTO result = estimationMapper.toDto(estimation);
//        estimationSearchRepository.save(estimation);
//        return result;
//    }

    /**
     * Get all the estimations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EstimationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Estimations");
        return estimationRepository.findAllWhereOrderNotWorkingCopy(pageable)
            .map(estimationMapper::toDto);
    }

    /**
     * Get one estimation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public EstimationDTO findOne(Long id) {
        log.debug("Request to get Estimation : {}", id);
        Estimation estimation = estimationRepository.findOne(id);
        estimation.getOperations().sort(Comparator.comparing(Operation::getId));
        return estimationMapper.toDto(estimation);
    }

    /**
     * Delete the  estimation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Estimation : {}", id);
        estimationRepository.delete(id);
        estimationSearchRepository.delete(id);
    }

    /**
     * Search for the estimation corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EstimationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Estimations for query {}", query);
        Page<Estimation> result = estimationSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(estimationMapper::toDto);
    }


    public ByteArrayOutputStream getTechnologyCard(Long estimationId){
        log.debug("Request for get technology card for estiamtionId: {}", estimationId);

        Estimation estimation = estimationRepository.findOne(estimationId);
        estimation.getOperations().size();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        technologyCardPdfCreator.createPdf(estimation,outputStream);
        return outputStream;

    }

    @Transactional(readOnly = true)
    public Page<EstimationDTO> findEstimationToFinish(Pageable pageable){

       Page<Estimation> estimation= estimationRepository.findAllToFinish(pageable,userService.getLoggedUser());
        return estimation.map(estimationMapper::toDto);
    }
}
