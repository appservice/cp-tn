package eu.canpack.fip.bo.operation.dto;

import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.operation.OperationEvent;
import eu.canpack.fip.bo.operation.enumeration.OperationEventType;
import eu.canpack.fip.bo.operation.enumeration.OperationStatus;
import eu.canpack.fip.bo.operator.OperatorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CP S.A.
 * Created by lukasz.mochel on 19.11.2017.
 */
public class OperationReportDTO {

    private static final Logger log=LoggerFactory.getLogger(OperationReportDTO.class);
    private Long id;

    private Integer sequenceNumber;

    private BigDecimal estimatedTime;

    private String machineName;

    private OperationStatus operationStatus;

    private Long estimationId;

    private BigDecimal operationPeriod;

    private List<OperationEventDTO> operationEvents = new ArrayList<>();

    public OperationReportDTO() {
    }

    public OperationReportDTO(Operation operation) {
        this.id = operation.getId();
        this.estimatedTime = operation.getEstimatedTime();
        this.machineName = operation.getMachine().getName();
        this.operationStatus = operation.getOperationStatus();
        this.sequenceNumber = operation.getSequenceNumber();
        this.estimationId = operation.getEstimation().getId();

        this.operationEvents = operation.getOperationEvents().stream()
            .map(evt -> {
                OperationEventDTO eventDTO = new OperationEventDTO();
                eventDTO.setId(evt.getId());
                eventDTO.setCreatedAt(evt.getCreatedAt());
                eventDTO.setOperationEventType(evt.getOperationEventType());
                OperatorDTO operatorDTO = new OperatorDTO();
                operatorDTO.setFirstName(evt.getOperator().getFirstName());
                operatorDTO.setLastName(evt.getOperator().getLastName());
                eventDTO.setOperator(operatorDTO);
                return eventDTO;

            }).collect(Collectors.toList());
        this.operationPeriod = calculateOperationPeriod(operation.getOperationEvents());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public BigDecimal getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(BigDecimal estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public OperationStatus getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(OperationStatus operationStatus) {
        this.operationStatus = operationStatus;
    }

    public List<OperationEventDTO> getOperationEvents() {
        return operationEvents;
    }

    public void setOperationEvents(List<OperationEventDTO> operationEvents) {
        this.operationEvents = operationEvents;
    }

    public Long getEstimationId() {
        return estimationId;
    }

    public void setEstimationId(Long estimationId) {
        this.estimationId = estimationId;
    }

    public BigDecimal getOperationPeriod() {
        return operationPeriod;
    }

    public void setOperationPeriod(BigDecimal operationPeriod) {
        this.operationPeriod = operationPeriod;
    }

    private BigDecimal calculateOperationPeriod(List<OperationEvent> operationEvents) {
        BigDecimal operationPeriod = BigDecimal.ZERO.setScale(3,RoundingMode.HALF_UP);

        ZonedDateTime beginTime = null;
        operationEvents.sort(Comparator.comparing(OperationEvent::getCreatedAt));

        for (OperationEvent event : operationEvents) {
            log.debug("event {}",event);
            if (event.getOperationEventType() == OperationEventType.START || event.getOperationEventType() == OperationEventType.RESUME) {
                beginTime = event.getCreatedAt();
            } else if (beginTime != null) {
                operationPeriod = operationPeriod.add(
                    BigDecimal.valueOf(Duration.between(beginTime, event.getCreatedAt()).toMillis())
                        .divide(BigDecimal.valueOf(3600000), 3, RoundingMode.HALF_UP));
            }
        }
        return operationPeriod;
    }

    @Override
    public String toString() {
        return "OperationReportDTO{" +
            "id=" + id +
            ", sequenceNumber=" + sequenceNumber +
            ", estimatedTime=" + estimatedTime +
            ", machineName='" + machineName + '\'' +
            ", operationStatus=" + operationStatus +
            ", operationEvents=" + operationEvents +
            '}';
    }
}
