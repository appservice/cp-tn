package eu.canpack.fip.bo.estimation;

import eu.canpack.fip.bo.attachment.Attachment;
import eu.canpack.fip.bo.attachment.AttachmentMapper;
import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.cooperation.Cooperation;
import eu.canpack.fip.bo.estimation.dto.EstimationCriteria;
import eu.canpack.fip.bo.estimation.dto.EstimationDTO;
import eu.canpack.fip.bo.estimation.dto.EstimationShowDTO;
import eu.canpack.fip.bo.machine.MachineDTO;
import eu.canpack.fip.bo.machine.MachineRepository;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.operation.dto.OperationDTO;
import eu.canpack.fip.bo.operation.dto.OperationMapper;
import eu.canpack.fip.bo.commercialPart.CommercialPart;
import eu.canpack.fip.bo.operation.enumeration.OperationType;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.OrderRepository;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import eu.canpack.fip.bo.pdf.TechnologyCardPdfCreator;
import eu.canpack.fip.bo.remark.EstimationRemark;
import eu.canpack.fip.domain.User;
import eu.canpack.fip.repository.UserRepository;
import eu.canpack.fip.bo.commercialPart.CommercialPartMapper;
import eu.canpack.fip.service.UserService;
import eu.canpack.fip.bo.cooperation.dto.CooperationMapper;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Estimation.
 */
@Service
@Transactional
public class EstimationService {

    private final Logger log = LoggerFactory.getLogger(EstimationService.class);

    private final EstimationRepository estimationRepository;

    private final EstimationMapper estimationMapper;


    private final OperationMapper operationMapper;

    private final CommercialPartMapper commercialPartMapper;

    private final UserRepository userRepository;

    private final TechnologyCardPdfCreator technologyCardPdfCreator;

    private final UserService userService;

    private final OrderRepository orderRepository;

    private final EstimationQueryService estimationQueryService;

    private final MachineRepository machineRepository;

    private final CooperationMapper cooperationMapper;

    private final AttachmentMapper attachmentMapper;

    public EstimationService(EstimationRepository estimationRepository, EstimationMapper estimationMapper, OperationMapper operationMapper, CommercialPartMapper commercialPartMapper, UserRepository userRepository, TechnologyCardPdfCreator technologyCardPdfCreator, UserService userService, OrderRepository orderRepository, EstimationQueryService estimationQueryService, MachineRepository machineRepository, CooperationMapper cooperationMapper, AttachmentMapper attachmentMapper) {
        this.estimationRepository = estimationRepository;
        this.estimationMapper = estimationMapper;
        this.operationMapper = operationMapper;
        this.commercialPartMapper = commercialPartMapper;
        this.userRepository = userRepository;
        this.technologyCardPdfCreator = technologyCardPdfCreator;
        this.userService = userService;
        this.orderRepository = orderRepository;


        this.estimationQueryService = estimationQueryService;
        this.machineRepository = machineRepository;
        this.cooperationMapper = cooperationMapper;
        this.attachmentMapper = attachmentMapper;
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
        log.debug("operations DTO: {}", estimationDTO.getOperations());
        log.debug("operations list: {}", operations);
        Order order = orderRepository.findOne(estimationDTO.getOrderId());

        Estimation finalEstimation = estimation;

        if (order.getOrderType().equals(OrderType.ESTIMATION)) {
            operations.forEach(operation -> {
                operation.setEstimation(finalEstimation);
                operation.setOperationType(OperationType.ESTIMATION);
            });

        } else {
            operations.forEach(operation -> {
                operation.setEstimation(finalEstimation);
                operation.setOperationType(OperationType.PRODUCTION);
            });
        }
        estimation.setOperations(operations);

        List<CommercialPart> commercialParts = commercialPartMapper.toEntity(estimationDTO.getCommercialParts());
        commercialParts.forEach(cp -> cp.setEstimation(finalEstimation));
        estimation.setCommercialParts(commercialParts);

        List<Cooperation> cooperationList = cooperationMapper.toEntity(estimationDTO.getCooperationList());
        cooperationList.forEach(co->co.setEstimation(finalEstimation));
        estimation.setCooperationList(cooperationList);


        estimation.setCreatedAt(ZonedDateTime.now());
        User currentUser = userService.getLoggedUser();
        estimation.setCreatedBy(currentUser);

        if (estimation.getDrawing() != null && estimationDTO.getDrawing()!=null && estimationDTO.getDrawing().getAttachments()!=null) {
//            List<Attachment>attachments=attachmentMapper.toEntity(estimationDTO.getDrawing().getAttachments());
//            for(Attachment attachment:attachments){
//                attachment.setDrawing(estimation.getDrawing());
//            }
//            estimation.getDrawing().setAttachments(attachments);
           // attachments.forEach(a->a.setDrawing(estimation.getDrawing()));
            log.debug("estimation DrawingO: {}", estimation.getDrawing());
            log.debug("attachmentsO: {}", estimation.getDrawing().getAttachments());

        }

        if (estimationDTO.getRemark() != null && !estimationDTO.getRemark().isEmpty()) {
            EstimationRemark remark = createRemark(estimationDTO.getRemark(), estimation);
            estimation.getEstimationRemarks().add(remark);
        }


        estimation = estimationRepository.save(estimation);
        EstimationDTO result = estimationMapper.toDto(estimation);

        return result;
    }

    private EstimationRemark createRemark(String remakr, Estimation estimation) {
        EstimationRemark estimationRemark = new EstimationRemark();
        estimationRemark.setCreatedAt(ZonedDateTime.now());
        estimationRemark.setCreatedBy(userService.getLoggedUser());
        estimationRemark.setEstimation(estimation);
        estimationRemark.setRemark(remakr);
        return estimationRemark;
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

      //  estimation.getOperations().sort(Comparator.comparing(Operation::getSequenceNumber));
        Set<Long>machineIds=estimation.getOperations().stream()
            .filter(op-> op.getMachine()!=null).map(o->o.getMachine().getId()).collect(Collectors.toSet());;

        EstimationDTO result=estimationMapper.toDto(estimation);

        if(!machineIds.isEmpty()){
            LocalDate creationDate;
            if(estimation.getCreatedAt()==null){
                creationDate=LocalDate.now();
            }else{
                creationDate=estimation.getCreatedAt().toLocalDate();
            }
           Map<Long, MachineDTO> machineDTOsMap = machineRepository.findAllByOperationDate(creationDate, machineIds)
               .stream().collect(Collectors.toMap(MachineDTO::getId, m -> m));

         for(OperationDTO operationDTO:result.getOperations()){
             operationDTO.setMachine(machineDTOsMap.get(operationDTO.getMachine().getId()));
         }

       }

        return result;
    }

    /**
     * Delete the  estimation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Estimation : {}", id);
        estimationRepository.delete(id);
    }



    public ByteArrayOutputStream getTechnologyCard(Long estimationId) {
        log.debug("Request for get technology card for estiamtionId: {}", estimationId);

        Estimation estimation = estimationRepository.findOne(estimationId);
        estimation.getOperations().size();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        technologyCardPdfCreator.createPdf(estimation, outputStream);
        return outputStream;

    }

    @Transactional(readOnly = true)
    public Page<EstimationDTO> findEstimationToFinish(Pageable pageable) {

        Page<Estimation> estimation = estimationRepository.findAllToFinish(pageable, userService.getLoggedUser());
        return estimation.map(estimationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<EstimationShowDTO> getAllInquiriesByCriteriaAndClient(EstimationCriteria criteria, Pageable pageable) {

        User user = userService.getLoggedUser();
        Set<Long> clientsId = user.getClients().stream().map(Client::getId).collect(Collectors.toSet());
        if (!clientsId.isEmpty() ) {
            log.debug("user clients ids: {}", clientsId);
            if (criteria.getClientId() == null) {
                criteria.setClientId(new LongFilter());
            }
            criteria.getClientId().setIn(new ArrayList<>(clientsId));

        }
        Page<Estimation> estimationPage = estimationQueryService.findByCriteria(criteria, pageable);
        return estimationPage.map(est->new EstimationShowDTO(est,est.isPricePublished()));
    }


    public void publishPrice(Long estimationId, Boolean isPublished){
      Estimation estimation=  estimationRepository.findOne(estimationId);
        estimation.setPricePublished(isPublished);

    }
}
