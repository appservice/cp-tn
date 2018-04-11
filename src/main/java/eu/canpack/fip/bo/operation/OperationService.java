package eu.canpack.fip.bo.operation;

import eu.canpack.fip.bo.operation.dto.*;
import eu.canpack.fip.bo.operation.enumeration.OperationEventType;
import eu.canpack.fip.bo.operation.enumeration.OperationStatus;
import eu.canpack.fip.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Operation.
 */
@Service
@Transactional
public class OperationService {

    private final Logger log = LoggerFactory.getLogger(OperationService.class);

    private final OperationRepository operationRepository;

    private final OperationMapper operationMapper;

    private final OperationWideMapper operationWideMapper;

    private final OperationEventMapper operationEventMapper;

    private final OperationEventRepository operationEventRepository;

    public OperationService(OperationRepository operationRepository, OperationMapper operationMapper, OperationWideMapper operationWideMapper, OperationEventMapper operationEventMapper, OperationEventRepository operationEventRepository) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
        this.operationWideMapper = operationWideMapper;
        this.operationEventMapper = operationEventMapper;
        this.operationEventRepository = operationEventRepository;
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
        if (operation == null) {
            return null;
        }
        OperationWideDTO operationWideDTO = operationWideMapper.toDto(operation);
        operationWideDTO.getOperationEvents().sort(Comparator.comparing(OperationEventDTO::getCreatedAt));
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


    public Long addOperationEvent(OperationEventDTO operationEventDTO) {
        OperationEvent operationEvent = operationEventMapper.toEntity(operationEventDTO);
        Operation operation = operationRepository.findOne(operationEventDTO.getOperationId());
        OperationEvent oe = operationEventRepository.save(operationEvent);
        operation.getOperationEvents().add(oe);
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
                operation.setOperationStatus(OperationStatus.RESUMED);
                break;
        }
        return oe.getId();

    }

    public void deleteOperationEvent(Long operationEventId) {
        OperationEvent oe = operationEventRepository.findOne(operationEventId);

        Operation operation = oe.getOperation();
        List<OperationEvent> events = operation.getOperationEvents();
        events.sort(Comparator.comparing(OperationEvent::getCreatedAt));
        if (!events.isEmpty() && !events.get(events.size() - 1).equals(oe)){
            throw new CustomParameterizedException("Deleted could be only last operation event");
        }

        operation.getOperationEvents().remove(oe);

        Optional<OperationEventType> operationEventTypeOptional = operation.getOperationEvents().stream()
            .sorted(Comparator.comparing(OperationEvent::getCreatedAt))
            .map(OperationEvent::getOperationEventType)
            .reduce((first, second) -> second);

        if (operationEventTypeOptional.isPresent()) {
            switch (operationEventTypeOptional.get()) {
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
                    operation.setOperationStatus(OperationStatus.RESUMED);
                    break;
            }
        } else {
            operation.setOperationStatus(null);
        }


        //operationEventRepository.delete(operationEventId);

    }
}
