package eu.canpack.fip.bo.operation;

import eu.canpack.fip.bo.operation.dto.*;
import eu.canpack.fip.bo.operation.enumeration.OperationEventType;
import eu.canpack.fip.bo.operation.enumeration.OperationStatus;
import eu.canpack.fip.repository.search.OperationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private final OperationEventMapper operationEventMapper;

    public OperationService(OperationRepository operationRepository, OperationMapper operationMapper, OperationSearchRepository operationSearchRepository, OperationWideMapper operationWideMapper, OperationEventMapper operationEventMapper) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
        this.operationSearchRepository = operationSearchRepository;
        this.operationWideMapper = operationWideMapper;
        this.operationEventMapper = operationEventMapper;
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

    @Transactional(readOnly = true)
    public List<OperationReportDTO> getOperationReports(Long estimationId) {

        return operationRepository.findOperationReportByEstimationId(estimationId).stream()
            .sorted(Comparator.comparing(Operation::getSequenceNumber))
            .map(OperationReportDTO::new).collect(Collectors.toList());
    }

    public void updateOperationsStatus(List<OperationReportDTO> operationReportList) {

        operationReportList.forEach(
            opDTO -> {
                Operation operation = operationRepository.findOne(opDTO.getId());
                operation.setOperationStatus(opDTO.getOperationStatus());
            }
        );
    }

    public void setAllOperationsFinished(Long estimationId) {
        List<Operation> operations = operationRepository.findAllByEstimationId(estimationId);
        operations.forEach(
            operation -> operation.setOperationStatus(OperationStatus.FINISHED)
        );

    }


    public void addOperationEvent(OperationEventDTO operationEventDTO) {
        OperationEvent operationEvent = operationEventMapper.toEntity(operationEventDTO);
        Operation operation = operationRepository.findOne(operationEventDTO.getOperationId());
        operation.getOperationEvents().add(operationEvent);
        switch (operationEventDTO.getOperationEventType()) {
            case START:
                operation.setOperationStatus(OperationStatus.STARTED);
                break;
            case PAUSE:
                operation.setOperationStatus(OperationStatus.PAUSED);
                break;
            case STOP:
                operation.setOperationStatus(OperationStatus.FINISHED);
                break;
            case RESUME:
                operation.setOperationStatus(OperationStatus.STARTED);
                break;
        }

    }
}
