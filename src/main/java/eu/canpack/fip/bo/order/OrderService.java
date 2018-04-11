package eu.canpack.fip.bo.order;

import eu.canpack.fip.bo.attachment.Attachment;
import eu.canpack.fip.bo.attachment.AttachmentDTO;
import eu.canpack.fip.bo.attachment.AttachmentRepository;
import eu.canpack.fip.bo.audit.Audit;
import eu.canpack.fip.bo.audit.AuditedOperation;
import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.client.ClientRepository;
import eu.canpack.fip.bo.commercialPart.CommercialPart;
import eu.canpack.fip.bo.cooperation.Cooperation;
import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.drawing.DrawingRepository;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.estimation.EstimationRepository;
import eu.canpack.fip.bo.estimation.dto.EstimationCreateDTO;
import eu.canpack.fip.bo.mpkBudgetMapper.MpkBudgetMapperService;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.operation.enumeration.OperationType;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import eu.canpack.fip.bo.order.dto.OrderListDTO;
import eu.canpack.fip.bo.order.dto.OrderMapper;
import eu.canpack.fip.bo.order.dto.OrderSimpleDTO;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import eu.canpack.fip.bo.referenceOrder.ReferenceOrder;
import eu.canpack.fip.bo.referenceOrder.ReferenceOrderRepository;
import eu.canpack.fip.bo.remark.EstimationRemark;
import eu.canpack.fip.config.ApplicationProperties;
import eu.canpack.fip.domain.User;
import eu.canpack.fip.repository.UserRepository;
import eu.canpack.fip.security.AuthoritiesConstants;
import eu.canpack.fip.security.SecurityUtils;
import eu.canpack.fip.service.UserService;
import eu.canpack.fip.web.rest.errors.CustomParameterizedException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Order.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final DrawingRepository drawingRepository;

    private final AttachmentRepository attachmentRepository;

    private final EstimationRepository estimationRepository;

    private final ApplicationProperties applicationProperties;

    private final ClientRepository clientRepository;

    private final UserService userService;

    private final ReferenceOrderRepository referenceOrderRepository;

    private final MpkBudgetMapperService mpkBudgetMapperService;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper,  UserRepository userRepository, DrawingRepository drawingRepository, AttachmentRepository attachmentRepository,
                        EstimationRepository estimationRepository, UserService userService, ApplicationProperties applicationProperties, ClientRepository clientRepository, ReferenceOrderRepository referenceOrderRepository, MpkBudgetMapperService mpkBudgetMapperService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.drawingRepository = drawingRepository;
        this.attachmentRepository = attachmentRepository;
        this.estimationRepository = estimationRepository;
        this.userService = userService;
        this.applicationProperties = applicationProperties;
        this.clientRepository = clientRepository;
        this.referenceOrderRepository = referenceOrderRepository;
        this.mpkBudgetMapperService = mpkBudgetMapperService;
    }

    /**
     * Save a order.
     *
     * @param orderListDTO the entity to save
     * @return the persisted entity
     */
    public OrderListDTO save(OrderListDTO orderListDTO) {
        log.debug("Request to save Order : {}", orderListDTO);
        Order order = orderMapper.toEntity(orderListDTO);
        order = orderRepository.save(order);
        OrderListDTO result = orderMapper.toDto(order);
        return result;
    }


    public Order createOrder(OrderDTO orderDTO) {
        log.debug("Request to create Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        User loggedUser = userService.getLoggedUser();


        ZonedDateTime now = ZonedDateTime.now();
        Client client = clientRepository.findOne(orderDTO.getClientId());
        String annualOrderNumber = client.getAnnualOrderNumber();

        List<Estimation> estimations = orderDTO.getEstimations().stream().map(estDTO -> {
            log.debug("osdDTO: {}", estDTO);
            Estimation estimation = new Estimation()
                .order(order)
                .description(estDTO.getDescription())
                .neededRealizationDate(estDTO.getNeededRealizationDate())
                .amount(estDTO.getAmount())
                .estimatedCost(estDTO.getEstimatedCost())
                .estimatedRealizationDate(estDTO.getEstimatedRealizationDate())
                .itemName(estDTO.getItemName())
                .itemNumber(estDTO.getItemNumber())
                .mpk(estDTO.getMpk());

            updateSapNumber(orderDTO, estDTO, estimation);


            if (annualOrderNumber != null && !annualOrderNumber.isEmpty()) {
                estimation.setSapNumber(annualOrderNumber);
            }

            updateRemark(loggedUser, estDTO, estimation);


            estimation.setId(estDTO.getId());

            if (estDTO.getDrawing() != null) {


                Drawing drawing;
                if (estDTO.getDrawing().getId() != null) {
                    drawing = drawingRepository.findOne(estDTO.getDrawing().getId());

                } else {
                    drawing = new Drawing();
                    drawing.getEstimations().add(estimation);
                    drawing.setNumber(estDTO.getItemNumber());
                    drawing.setName(estDTO.getItemName());
                    drawing.setCreatedAt(now);

                    List<Attachment> attahcments = new ArrayList<>();
                    for (AttachmentDTO a : estDTO.getDrawing().getAttachments()) {
                        Attachment one = attachmentRepository.findOne(a.getId());
                        one.setDrawing(drawing);
                        attahcments.add(one);
                    }

                    drawing.setAttachments(attahcments);
                    drawing = drawingRepository.save(drawing);

                }


                estimation.setDrawing(drawing);
            }

            return estimation;

        }).collect(Collectors.toList());
        order.setEstimations(estimations);
        order.setOfferRemarks(applicationProperties.getInitialOfferRemarks().replace("|", "\n"));


        order.createdAt(now);
        order.setCreatedBy(loggedUser);
        prepareDocumentNumber(order);

//        order.setOrderStatus(OrderStatus.WORKING_COPY);
        log.debug("Order to save: {}", order);

        Audit audit = new Audit()
            .createdAt(now)
            .createdBy(loggedUser)
            .operation(AuditedOperation.CREATED)
            .order(order);
        if(order.getOrderStatus()==OrderStatus.SENT_TO_ESTIMATION){
            audit.setOperation(AuditedOperation.CREATED_AND_SEND_TO_ESTIMATION);
        }
        order.getAudits().add(audit);

        return orderRepository.save(order);

    }

    public OrderListDTO updateOrder(OrderDTO orderDTO) {
        User loggedUser = userService.getLoggedUser();

        ZonedDateTime now = ZonedDateTime.now();

        log.debug("Request to update Order : {}", orderDTO);
        Order orderFromDb = orderRepository.findOne(orderDTO.getId());
        Client client = clientRepository.findOne(orderDTO.getClientId());
        String annualOrderNumber = client.getAnnualOrderNumber();

        Order order = orderMapper.toEntity(orderDTO);
        order.setReferenceOrders(orderFromDb.getReferenceOrders());
        order.estimationMaker(orderFromDb.getEstimationMaker());

        order.setYear(orderFromDb.getYear());
        order.setInquiryNumber(orderFromDb.getInquiryNumber());
        order.setPurchaseOrderNumber(orderFromDb.getPurchaseOrderNumber());
        order.setEmergencyOrderNumber(orderFromDb.getEmergencyOrderNumber());


        List<Estimation> newEstimations = getNewEstimations(orderDTO, loggedUser, now, annualOrderNumber, order);

        List<Estimation> oldEstimation = getOldEstimations(orderDTO, loggedUser);
        order.setEstimations(oldEstimation);
        order.getEstimations().addAll(newEstimations);


        order.setCreatedBy(loggedUser);

        order.createdAt(now);

        Audit audit = new Audit()
            .createdAt(now)
            .createdBy(loggedUser)
            .operation(AuditedOperation.UPDATED)
            .order(order);
        if(order.getOrderStatus()==OrderStatus.SENT_TO_ESTIMATION){
            audit.setOperation(AuditedOperation.UPDATED_AND_SEND_TO_ESTIMATION);
        }
        Set<Audit> audits = orderFromDb.getAudits();
        audits.add(audit);
        order.getAudits().addAll(audits);
        log.debug("Order to save: {}", order);

        return orderMapper.toDto(orderRepository.save(order));

    }

    public OrderListDTO updateOrderAsAdmin(OrderDTO orderDTO) {
        User loggedUser = userService.getLoggedUser();

        ZonedDateTime now = ZonedDateTime.now();

        log.debug("Request to update Order : {}", orderDTO);
        Order orderFromDb = orderRepository.findOne(orderDTO.getId());
        Client client = clientRepository.findOne(orderDTO.getClientId());
        String annualOrderNumber = client.getAnnualOrderNumber();

        Order order = orderMapper.toEntity(orderDTO);
        order.estimationMaker(orderFromDb.getEstimationMaker());
        order.setReferenceOrders(orderFromDb.getReferenceOrders());

        order.setYear(orderFromDb.getYear());
        order.setInquiryNumber(orderFromDb.getInquiryNumber());
        order.setPurchaseOrderNumber(orderFromDb.getPurchaseOrderNumber());
        order.setEmergencyOrderNumber(orderFromDb.getEmergencyOrderNumber());


        List<Estimation> newEstimations = getNewEstimations(orderDTO, loggedUser, now, annualOrderNumber, order);

        List<Estimation> oldEstimation = getOldEstimations(orderDTO, loggedUser);
        order.setEstimations(oldEstimation);
        order.getEstimations().addAll(newEstimations);


        order.setCreatedBy(orderFromDb.getCreatedBy());
        order.createdAt(orderFromDb.getCreatedAt());
        order.setOrderStatus(orderFromDb.getOrderStatus());

        log.debug("Order to save: {}", order);
        Audit audit = new Audit()
            .createdAt(now)
            .createdBy(loggedUser)
           .operation(AuditedOperation.UPDATED_BY_ADMIN)

            .order(order);


        Set<Audit> audits = orderFromDb.getAudits();
        audits.add(audit);
        order.getAudits().addAll(audits);

        return orderMapper.toDto(orderRepository.save(order));

    }

    private List<Estimation> getOldEstimations(OrderDTO orderDTO, User loggedUser) {
        ZonedDateTime now = ZonedDateTime.now();
        return orderDTO.getEstimations().stream()
            .filter(est -> est.getId() != null)
            .map(estDTO -> {
                Estimation estimation = estimationRepository.findOne(estDTO.getId());
                estimation.setDescription(estDTO.getDescription());
                estimation.setAmount(estDTO.getAmount());
                estimation.setNeededRealizationDate(estDTO.getNeededRealizationDate());

                estimation.setDescription(estDTO.getDescription());
                estimation.setItemName(estDTO.getItemName());
                estimation.setItemNumber(estDTO.getItemNumber());
                estimation.setMpk(estDTO.getMpk());
                updateSapNumber(orderDTO, estDTO, estimation);

                updateRemark(loggedUser, estDTO, estimation);

                estimation.setId(estDTO.getId());

                if (estDTO.getDrawing() != null) {
                    List<Attachment> attachments = estDTO.getDrawing().getAttachments().stream()
                        .map(a -> attachmentRepository.findOne(a.getId())).collect(Collectors.toList());

                    if (estimation.getDrawing() != null) {
                        estimation.getDrawing().setNumber(estDTO.getItemNumber());
                        estimation.getDrawing().setName(estDTO.getItemName());
                        for(Attachment attachment:attachments){
                            attachment.setDrawing(estimation.getDrawing());
                        }
                        estimation.getDrawing().setAttachments(attachments);


                    } else {
                        Drawing drawing = new Drawing();
                        drawing.setName(estDTO.getItemName());
                        drawing.setNumber(estDTO.getItemNumber());
                        for(Attachment attachment:attachments){
                            attachment.setDrawing(drawing);
                        }
                        drawing.setAttachments(attachments);
                        drawing.getEstimations().add(estimation);
                        drawing.setCreatedAt(now);
                        estimation.setDrawing(drawing);
                        drawingRepository.save(drawing);
                    }

                }


                return estimation;
            }).collect(Collectors.toList());
    }

    private List<Estimation> getNewEstimations(OrderDTO orderDTO, User loggedUser, ZonedDateTime now, String annualOrderNumber, Order order) {
        return orderDTO.getEstimations().stream()
            .filter(es -> es.getId() == null)
            .map(estDTO -> {
                Estimation estimation = new Estimation()
                    .order(order)
                    .description(estDTO.getDescription())
                    .neededRealizationDate(estDTO.getNeededRealizationDate())
                    .amount(estDTO.getAmount())
                    .itemName(estDTO.getItemName())
                    .itemNumber(estDTO.getItemNumber())
                    .mpk(estDTO.getMpk());

                updateSapNumber(orderDTO, estDTO, estimation);


                updateRemark(loggedUser, estDTO, estimation);
                if (annualOrderNumber != null && !annualOrderNumber.isEmpty()) {
                    estimation.setSapNumber(annualOrderNumber);
                }

                estimation.setId(estDTO.getId());


                if (estDTO.getDrawing() != null) {

                    Drawing drawing = new Drawing();
                    drawing.getEstimations().add(estimation);
                    drawing.setNumber(estDTO.getItemNumber());
                    drawing.setName(estDTO.getItemName());
                    drawing.setId(estDTO.getDrawing().getId());
                    List<Attachment> attahcments = new ArrayList<>();
                    for (AttachmentDTO a : estDTO.getDrawing().getAttachments()) {
                        Attachment one = attachmentRepository.findOne(a.getId());
                        one.setDrawing(drawing);
                        attahcments.add(one);
                    }
                    drawing.setAttachments(attahcments);
                    drawing.setCreatedAt(now);
                    drawingRepository.save(drawing);
                    estimation.setDrawing(drawing);
                }
                return estimation;

            }).collect(Collectors.toList());
    }

    private void updateSapNumber(OrderDTO orderDTO, EstimationCreateDTO estDTO, Estimation estimation) {


        if (orderDTO.getOrderType() == OrderType.EMERGENCY
            && estDTO.getMpk() != null
            && orderDTO.getClientId() != null) {

            mpkBudgetMapperService.findOneByMpkAndClientId(estDTO.getMpk(), orderDTO.getClientId())
                .ifPresent(mpkBudgetMapper ->
                               estimation.setSapNumber(mpkBudgetMapper.getBudget())
                );


        }
    }

    private void updateRemark(User loggedUser, EstimationCreateDTO estDTO, Estimation estimation) {
        if (estDTO.getRemark() != null && !estDTO.getRemark().isEmpty()) {

            EstimationRemark remark = new EstimationRemark();
            remark.setRemark(estDTO.getRemark());
            remark.setCreatedAt(ZonedDateTime.now());
            remark.setCreatedBy(loggedUser);
            remark.setEstimation(estimation);
            estimation.getEstimationRemarks().add(remark);
        }
    }


    /**
     * Get all the orders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderListDTO> findAllByClientAndOrderType(Pageable pageable, OrderType orderType) {
        log.debug("Request to get all Orders");
        User user = userService.getLoggedUser();
        Set<Client> clients = user.getClients();
        if (clients != null) {
            return orderRepository.findAllByClientInAndOrderType(clients, orderType, pageable).map(orderMapper::toDto);
        }
        return orderRepository.findAllByOrderType(orderType, pageable)
            .map(orderMapper::toDto);
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OrderListDTO findOne(Long id) {
        log.debug("Request to get Order : {}", id);

        Order order = orderRepository.findOne(id);
        secureAccessability(order);

        return orderMapper.toDto(order);
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OrderDTO getOrder(Long id) {
        log.debug("Request to get Order : {}", id);
        Order order = orderRepository.findOne(id);
        OrderDTO orderDTO = new OrderDTO(order);
        secureAccessability(order);

        order.getEstimations().size();

        List<EstimationCreateDTO> estimationCreateDTOS = order.getEstimations().stream()
            .map(EstimationCreateDTO::new)
            .collect(Collectors.toList());
        orderDTO.setEstimations(estimationCreateDTOS);
        orderDTO.setCanEdit(canEditOrder(order));
        orderDTO.setCanEditAsAdmin(canEditOrderAsAdmin(order));
        return orderDTO;

    }

    private boolean canEditOrder(Order order) {
        return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ORDER_INTRODUCER) && (order.getOrderStatus() == OrderStatus.WORKING_COPY || order.getOrderStatus() == null);

    }

    private boolean canEditOrderAsAdmin(Order order) {
        return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);

    }

    private boolean canCreateOrder(Order order) {
        return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ORDER_INTRODUCER)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OrderDTO getInquiryForClient(Long id) {
        log.debug("Request to get Order : {}", id);

        Order order = orderRepository.findOne(id);

        secureAccessability(order);
        if (order == null || order.getOrderType() != OrderType.ESTIMATION) {
            return null;
        }


        OrderDTO orderDTO = new OrderDTO(order);

        order.getEstimations().size();

        List<EstimationCreateDTO> estimationCreateDTOS = order.getEstimations().stream()
            .map(estimation -> new EstimationCreateDTO(estimation, estimation.isPricePublished()))
            .collect(Collectors.toList());
        orderDTO.setEstimations(estimationCreateDTOS);
        return orderDTO;

    }

    private void secureAccessability(Order order) {
        User currentUser = userService.getLoggedUser();
        if (!currentUser.getClients().isEmpty()) {
            if (!currentUser.getClients().contains(order.getClient())) {
                throw new AccessDeniedException("Can not get this resource");
            }
        }
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OrderDTO getOrderEstimated(Long id) {
        log.debug("Request to get Order estimated : {}", id);
        Order order = orderRepository.findOne(id);

        List<Estimation> estimations = order.getEstimations().stream()
            .filter(e -> e.getEstimatedCost() != null && e.getEstimatedCost().compareTo(BigDecimal.ZERO) > 0)
//            .peek(est -> est.getOperations().forEach(o -> o.setId(null)))
//            .peek(e -> e.setId(null))
            .collect(Collectors.toList());
        order.setEstimations(estimations);

        OrderDTO orderDTO = new OrderDTO(order);

        order.getEstimations().size();

        List<EstimationCreateDTO> estimationCreateDTOS = order.getEstimations().stream()
            .map(EstimationCreateDTO::new)
            .collect(Collectors.toList());
        orderDTO.setEstimations(estimationCreateDTOS);
        orderDTO.setCanEdit(canCreateOrder(order));
        return orderDTO;

    }

    /**
     * Delete the  order by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        Order order = orderRepository.findOne(id);
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MANAGER)
            || order.getOrderStatus() == OrderStatus.WORKING_COPY) {

            Set<ReferenceOrder> referenceOrderToDelete = referenceOrderRepository.findAllByRefOrderId(id);
            log.debug("refOrders to delete {}", referenceOrderToDelete);


            orderRepository.delete(id);
            referenceOrderRepository.deleteInBatch(referenceOrderToDelete);

        } else {
            throw new CustomParameterizedException("error.userNotHaveSuitablePermissions");
        }

    }


    private void prepareDocumentNumber(Order order) {
        int year = LocalDate.now().getYear();
        int number = 0;
        switch (order.getOrderType()) {
            case ESTIMATION:
                number = orderRepository.getInquiryNumber(year) + 1;
                order.setInquiryNumber(number);
                break;
            case EMERGENCY:
                number = orderRepository.getEmergencyOrderNumber(year) + 1;
                order.setEmergencyOrderNumber(number);
                break;
            case PRODUCTION:
                number = orderRepository.getPurchaseOrderDocNumber(year) + 1;
                order.setPurchaseOrderNumber(number);
                break;

        }
        order.setYear(year);


        order.setInternalNumber(number + "/" + year + "/" + order.getOrderType().getPlName());

    }

    private void preparePurchaseOrderDocumentNumber(Order order) {
        int year = LocalDate.now().getYear();
        int number = orderRepository.getPurchaseOrderDocNumber(year) + 1;
        order.setYear(year);
        order.setPurchaseOrderNumber(number);

        order.setInternalNumber(number + "/" + year + "/" + order.getOrderType().getPlName());

    }

    OrderSimpleDTO getSimpleOrderDtoData(Long orderId) {
        return orderRepository.getOrderSimpleDTO(orderId);
    }


    @Transactional(readOnly = true)
    public Page<OrderListDTO> findOrderToClaimEstimation(Pageable pageable) {
        log.debug("Request to search for a page of Orders for findOrderToClaimEstimation ");
        Page<Order> result = orderRepository.findOrdersClaimToEstimation(pageable);
        return result.map(orderMapper::toDto);
    }


    public void claimByEstimatior(Long orderId) {

        Order order = orderRepository.findOne(orderId);
        order.setEstimationMaker(userService.getLoggedUser());
        order.setOrderStatus(OrderStatus.IN_ESTIMATION);
        Audit audit = new Audit()
            .order(order)
            .operation(AuditedOperation.CLAIMED_ESTIMATION)
            .createdAt(ZonedDateTime.now())
            .createdBy(userService.getLoggedUser());
        order.getAudits().add(audit);
    }

    @Transactional(readOnly = true)
    Page<Order> findOrderToEstimationByUser(Pageable pageable) {
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            return orderRepository.findOrdersByOrderStatus(OrderStatus.IN_ESTIMATION, pageable);
        }
        User logedUser = userService.getLoggedUser();
        return orderRepository.findOrderToEstimationByUser(logedUser.getId(), pageable);
    }


   public void moveOrderToArchive(Long id) {
        log.debug("moveOrderToArchive with id {}", id);

        Order order = orderRepository.findOne(id);
        order.setOrderStatus(OrderStatus.SENT_OFFER_TO_CLIENT);
        order.setEstimationFinsihDate(ZonedDateTime.now());
        Audit audit = new Audit()
            .order(order)
            .operation(AuditedOperation.MOVED_TO_ARCHIVE)
            .createdAt(ZonedDateTime.now())
            .createdBy(userService.getLoggedUser());
        order.getAudits().add(audit);
        orderRepository.save(order);
    }

    @Transactional
    public Order createPurchaseOrder(OrderDTO inquiryDTO) {

        log.debug("createPurchaseOrder orderDTO: {}", inquiryDTO);
        Order inquiry = orderRepository.findOne(inquiryDTO.getInquiryId());
        Order order = orderMapper.toEntity(inquiryDTO);
        order.setEstimationMaker(inquiry.getEstimationMaker());
        order.setId(null);


        User loggedUser = userService.getLoggedUser();
        ZonedDateTime now = ZonedDateTime.now();

//        PropertyFilter filter = PropertyFilters.getAnnotationFilter(Id.class);

        for (EstimationCreateDTO estDTO : inquiryDTO.getEstimations()) {
            Estimation est = estimationRepository.findOne(estDTO.getId());
            est.getCommercialParts().size();
            est.getOperations().size();

            Estimation newEstimation = new Estimation();
            newEstimation.setAmount(estDTO.getAmount());
            if (est.getExecutionTimeValue() != null) {
                newEstimation.setEstimatedRealizationDate(now.toLocalDate().plus(est.getExecutionTimeValue(), est.getExecutionTimeUnit()));
            } else {
                newEstimation.setEstimatedRealizationDate(est.getEstimatedRealizationDate());

            }
            newEstimation.setEstimatedCost(est.getEstimatedCost());
            newEstimation.setDrawing(est.getDrawing());
            newEstimation.setDescription(est.getDescription());
            newEstimation.setItemNumber(est.getItemNumber());
            newEstimation.setItemName(est.getItemName());
            newEstimation.setCreatedBy(est.getCreatedBy());
            newEstimation.setCreatedAt(est.getCreatedAt());
            newEstimation.setMass(est.getMass());
            newEstimation.setMaterial(est.getMaterial());
            newEstimation.setNeededRealizationDate(est.getNeededRealizationDate());
            newEstimation.setMaterialPrice(est.getMaterialPrice());
            newEstimation.setMpk(estDTO.getMpk());
            newEstimation.setMaterialType(est.getMaterialType());
            newEstimation.setPricePublished(true);

            if (est.getMpk() != null && inquiryDTO.getClientId() != null) {
                mpkBudgetMapperService.findOneByMpkAndClientId(est.getMpk(), inquiryDTO.getClientId())
                    .ifPresent(mpkBudgetMapper ->
                                   newEstimation.setSapNumber(mpkBudgetMapper.getBudget())
                    );
            }


            if (estDTO.getRemark() != null && !estDTO.getRemark().trim().isEmpty()) {
                EstimationRemark estimationRemark = new EstimationRemark();
                estimationRemark.setRemark(estDTO.getRemark());
                estimationRemark.setCreatedBy(loggedUser);
                estimationRemark.setCreatedAt(now);
                estimationRemark.setEstimation(newEstimation);
                newEstimation.getEstimationRemarks().add(estimationRemark);
            }


            for (Operation op : est.getOperations()) {
                Operation newOperation = new Operation();
                newOperation.setEstimation(newEstimation);
                newOperation.setDescription(op.getDescription());
                newOperation.setRealTime(op.getRealTime());
                newOperation.setMachine(op.getMachine());
                newOperation.setSequenceNumber(op.getSequenceNumber());
                newOperation.setRemark(op.getRemark());
                newOperation.setEstimatedTime(op.getEstimatedTime());
                newOperation.setOperationType(OperationType.PRODUCTION);

                newEstimation.getOperations().add(newOperation);
            }

            for (CommercialPart cp : est.getCommercialParts()) {
                CommercialPart newCp = new CommercialPart();
                newCp.setEstimation(newEstimation);
                newCp.setAmount(cp.getAmount());
                newCp.setPrice(cp.getPrice());
                newCp.setName(cp.getName());
                newCp.setUnit(cp.getUnit());
                newEstimation.getCommercialParts().add(newCp);
            }

            for (Cooperation coop : est.getCooperationList()) {
                Cooperation newCooperatoin = new Cooperation();
                newCooperatoin.setEstimation(newEstimation);
                newCooperatoin.setAmount(coop.getAmount());
                newCooperatoin.setPrice(coop.getPrice());
                newCooperatoin.setName(coop.getName());
                newCooperatoin.setCounterparty(coop.getCounterparty());
                newEstimation.getCooperationList().add(newCooperatoin);
            }
            newEstimation.setOrder(order);
            order.getEstimations().add(newEstimation);
        }


        order.setCreatedAt(now);
        order.createdBy(loggedUser);
        preparePurchaseOrderDocumentNumber(order);
        if (!inquiryDTO.getOrderStatus().equals(OrderStatus.WORKING_COPY)) {
            order.setOrderStatus(OrderStatus.CREATING_SAP_ORDER);

        }

        //  inquiry.setOrderStatus(OrderStatus.CREATED_PURCHASE_ORDER);
        order = orderRepository.save(order);
        ReferenceOrder purchaseReferenceOrder = createReferenceOrder(inquiry, order);
        ReferenceOrder inquiryReferenceOrder = createReferenceOrder(order, inquiry);
        order.getReferenceOrders().add(inquiryReferenceOrder);
        inquiry.getReferenceOrders().add(purchaseReferenceOrder);

        Audit audit = new Audit()
            .order(order)
            .operation(AuditedOperation.CREATED)
            .createdAt(now)
            .createdBy(loggedUser);
        order.getAudits().add(audit);

        return order;

    }

    private ReferenceOrder createReferenceOrder(Order inquiry, Order purchaseOrder) {
        ReferenceOrder referenceOrder = new ReferenceOrder();
        referenceOrder.setOrder(inquiry);
        referenceOrder.setRefOrderId(purchaseOrder.getId());
        referenceOrder.setRefInternalNumber(purchaseOrder.getInternalNumber());
        return referenceOrder;
    }

    @Transactional(readOnly = true)
    public Page<OrderListDTO> findOrderInProducitionForEdit(Pageable pageable) {
        Set<OrderStatus> orderStatuses = new HashSet<>();
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.TECHNOLOGIST)) {
            orderStatuses.add(OrderStatus.TECHNOLOGY_VERIFICATION);
            orderStatuses.add(OrderStatus.TECHNOLOGY_CREATION);

        }
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.TEAM_LEADER)) {
            orderStatuses.add(OrderStatus.TECHNOLOGY_CREATION);

        }
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SAP_INTRODUCER)) {
            orderStatuses.add(OrderStatus.CREATING_SAP_ORDER);
        }
        log.debug("order statusses", orderStatuses);
        if (!orderStatuses.isEmpty()) {
            Page<Order> orders = orderRepository.findAllByOrderTypeAndOrderStatusIn(orderStatuses, pageable);//OrderType.PRODUCTION,
            return orders.map(orderMapper::toDto);
        }


        return new PageImpl<>(Collections.emptyList());
    }

    @Transactional
    public OrderDTO insertSapNumbers(OrderDTO orderDTO) {
        orderDTO.getEstimations().forEach(e -> {
            Estimation estimation = estimationRepository.findOne(e.getId());
            estimation.setSapNumber(e.getSapNumber());
            estimation.setMpk(e.getMpk());
        });
        Order order = orderRepository.findOne(orderDTO.getId());
        boolean hasOrderEmptySapNumber = order.getEstimations().stream()
            .anyMatch(e -> e.getSapNumber() == null || e.getSapNumber().isEmpty());
        if (!hasOrderEmptySapNumber && order.getOrderStatus()==OrderStatus.CREATING_SAP_ORDER) {
            switch (order.getOrderType()) {
                case PRODUCTION:
                    order.setOrderStatus(OrderStatus.TECHNOLOGY_VERIFICATION);
                    break;
                case EMERGENCY:
                    order.setOrderStatus(OrderStatus.TECHNOLOGY_CREATION);

            }
        }
        Audit audit = new Audit()
            .order(order)
            .operation(AuditedOperation.FILLED_SAP_NUMBER)
            .createdAt(ZonedDateTime.now())
            .createdBy(userService.getLoggedUser());
        order.getAudits().add(audit);


        return new OrderDTO(order);

    }

    @Transactional
    public void moveOrderToProduction(Long id) {
        Order order = orderRepository.findOne(id);
        order.setOrderStatus(OrderStatus.IN_PRODUCTION);
        ZonedDateTime now = ZonedDateTime.now();


        order.getEstimations()
            .forEach(e -> {
                e.setInProduction(true);
                e.setProductionStartDateTime(now);
            });
        Audit audit = new Audit()
            .order(order)
            .operation(AuditedOperation.MOVED_TO_PRODUCTION)
            .createdAt(ZonedDateTime.now())
            .createdBy(userService.getLoggedUser());
        order.getAudits().add(audit);
        orderRepository.save(order);
    }


    public void saveOfferRemarks(Long orderId, String text) {
        Order order = orderRepository.findOne(orderId);
        order.setOfferRemarks(text);
    }

    public void removeAssingedEstimator(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.setEstimationMaker(null);
        order.setOrderStatus(OrderStatus.SENT_TO_ESTIMATION);
        Audit audit = new Audit()
            .order(order)
            .operation(AuditedOperation.UNCLAIMED_ESTIMATION)
            .createdAt(ZonedDateTime.now())
            .createdBy(userService.getLoggedUser());
        order.getAudits().add(audit);

    }

    public Order cloneOrder(Long orderId) {
        Order oldOrder = orderRepository.findOne(orderId);

        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.WORKING_COPY);
        newOrder.setOrderType(oldOrder.getOrderType());
        newOrder.setDeliveryAddress(oldOrder.getDeliveryAddress());
        newOrder.setClient(oldOrder.getClient());
        newOrder.setCreatedBy(userService.getLoggedUser());
        newOrder.setCreatedAt(ZonedDateTime.now());
        newOrder.setOfferRemarks(oldOrder.getOfferRemarks());
        newOrder.setName(oldOrder.getName());
        prepareDocumentNumber(newOrder);

        oldOrder.getEstimations()
            .forEach(estimation -> {
                Estimation newEstimation = new Estimation();
                newEstimation.setMpk(estimation.getMpk());
                newEstimation.setPricePublished(false);
                newEstimation.setOrder(newOrder);
                newEstimation.setSapNumber(estimation.getSapNumber());
                newEstimation.setItemName(estimation.getItemName());
                newEstimation.setItemNumber(estimation.getItemNumber());
                newEstimation.setAmount(estimation.getAmount());
                newEstimation.setDescription(estimation.getDescription());
                newEstimation.setMaterialType(estimation.getMaterialType());
                newEstimation.setMaterial(estimation.getMaterial());
                newEstimation.setMass(estimation.getMass());
                newEstimation.setSapNumber(estimation.getSapNumber());
                newEstimation.setExecutionTimeUnit(estimation.getExecutionTimeUnit());
                newEstimation.setExecutionTimeValue(estimation.getExecutionTimeValue());
                newEstimation.setDrawing(estimation.getDrawing());
                estimation.getOperations()
                    .forEach(operation -> {
                        Operation newOperation = new Operation();
                        newOperation.setSequenceNumber(operation.getSequenceNumber());
                        newOperation.setEstimation(newEstimation);
                        newOperation.setOperationType(operation.getOperationType());
                        newOperation.setDescription(operation.getDescription());
                        newOperation.setEstimatedTime(operation.getEstimatedTime());
                        newOperation.setMachine(operation.getMachine());
                        newEstimation.getOperations().add(newOperation);
                    });
                estimation.getCommercialParts()
                    .forEach(commercialPart -> {
                        CommercialPart newCommercialPart = new CommercialPart();
                        newCommercialPart.setEstimation(newEstimation);
                        newCommercialPart.setAmount(commercialPart.getAmount());
                        newCommercialPart.setName(commercialPart.getName());
                        newCommercialPart.setPrice(commercialPart.getPrice());
                        newCommercialPart.setUnit(commercialPart.getUnit());
                        newEstimation.getCommercialParts().add(newCommercialPart);
                    });
                estimation.getCooperationList()
                    .forEach(cooperation -> {
                        Cooperation newCooperation = new Cooperation();
                        newCooperation.setAmount(cooperation.getAmount());
                        newCooperation.setCounterparty(cooperation.getCounterparty());
                        newCooperation.setEstimation(newEstimation);
                        newCooperation.setName(cooperation.getName());
                        newCooperation.setPrice(cooperation.getPrice());
                        newEstimation.getCooperationList().add(newCooperation);
                    });


                Drawing oldDrawing = estimation.getDrawing();
                Drawing newDrawing = new Drawing();
                newDrawing.setNumber(oldDrawing.getNumber());
                newDrawing.setName(oldDrawing.getName());
                newDrawing.getEstimations().add(estimation);
                newDrawing.setCreatedAt(ZonedDateTime.now());

                oldDrawing.getAttachments().forEach(attachment -> {
                    Attachment newAttachment = new Attachment();
                    newAttachment.setDataContentType(attachment.getDataContentType());
                    newAttachment.setUploadDate(attachment.getUploadDate());
                    newAttachment.setName(attachment.getName());
                    newAttachment.setDrawing(newDrawing);

                    String newPath = copyAttachmentFile(attachment.getPath());
                    newAttachment.setPath(newPath);
                    newDrawing.getAttachments().add(newAttachment);
                });


                newEstimation.setDrawing(drawingRepository.save(newDrawing));
                newOrder.getEstimations().add(newEstimation);

            });
        Audit audit = new Audit()
            .order(newOrder)
            .operation(AuditedOperation.CLONED)
            .createdAt(ZonedDateTime.now())
            .createdBy(userService.getLoggedUser());
        newOrder.getAudits().add(audit);

        return orderRepository.save(newOrder);

    }

    protected String copyAttachmentFile(String attachmentPath) {
        ZonedDateTime now = ZonedDateTime.now();
        Path path = Paths.get(attachmentPath);

        String fileName = path.toFile().getName();
        String dateString = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss_SSS"));
        String fileExtensions = FilenameUtils.getExtension(fileName);
        String fileBaseName = FilenameUtils.getBaseName(fileName);
        log.debug("fileBaseName: {}", fileBaseName);
        String newFileName = fileBaseName.substring(0, fileBaseName.length() - 19).concat(dateString) + "." + fileExtensions;
        try {
            Path newPath = Files.copy(path, Paths.get(path.getParent().toAbsolutePath().toString(), newFileName));
            return newPath.toAbsolutePath().toString();
        } catch (IOException e) {
            log.error("can not copy file {}", attachmentPath, e);
            throw new CustomParameterizedException("error.copyAttachemntFileError", attachmentPath);
        }
    }
}
