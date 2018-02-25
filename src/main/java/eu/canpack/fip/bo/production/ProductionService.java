package eu.canpack.fip.bo.production;

import eu.canpack.fip.bo.audit.Audit;
import eu.canpack.fip.bo.audit.AuditedOperation;
import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.estimation.EstimationQueryService;
import eu.canpack.fip.bo.estimation.EstimationRepository;
import eu.canpack.fip.bo.estimation.dto.EstimationCriteria;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.OrderRepository;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.domain.User;
import eu.canpack.fip.security.AuthoritiesConstants;
import eu.canpack.fip.security.SecurityUtils;
import eu.canpack.fip.service.UserService;
import eu.canpack.fip.web.rest.errors.CustomParameterizedException;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.ShellProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CP S.A.
 * Created by lukasz.mochel on 29.10.2017.
 */
@Service
@Transactional
public class ProductionService {

    private static final Logger log = LoggerFactory.getLogger(ProductionService.class);

    private final UserService userService;

    private final EstimationRepository estimationRepository;

    private final EstimationQueryService estimationQueryService;

    private final ProductionExcelService productionExcelService;

    private final FinishedProductionExcelService finishedProductionExcelService;

    private final OrderRepository orderRepository;

    public ProductionService(UserService userService, EstimationRepository estimationRepository, EstimationQueryService estimationQueryService, ProductionExcelService productionExcelService, FinishedProductionExcelService finishedProductionExcelService, OrderRepository orderRepository) {
        this.userService = userService;
        this.estimationRepository = estimationRepository;
        this.estimationQueryService = estimationQueryService;
        this.productionExcelService = productionExcelService;
        this.finishedProductionExcelService = finishedProductionExcelService;
        this.orderRepository = orderRepository;
    }

    public Page<ProductionItemDTO> showActualProduction(Pageable pageable) {
        return estimationRepository.getItemsActualInProduction(pageable);
    }

//    public Page<ProductionItemDTO> showActualProductionFiltered(Pageable pageable){
//
//    };


    @Transactional(readOnly = true)
    public Page<ProductionItemDTO> showActualInProductionByCriteriaAndClient(EstimationCriteria criteria, Pageable pageable) {

        Page<Estimation> estimationPage = findEstimationsByCriteria(criteria, pageable);
        return estimationPage.map(ProductionItemDTO::new);
    }


    @Transactional(readOnly = true)
    public List<ProductionItemDTO> showActualInProductionByCriteriaAndClient(EstimationCriteria criteria, Sort sort) {

        List<Estimation> estimationList = findEstimationsByCriteria(criteria, sort);
        return estimationList.stream().map(ProductionItemDTO::new).collect(Collectors.toList());//estimationPage.map(ProductionItemDTO::new);
    }

    @Transactional(readOnly = true)
    public byte[] makeExcelOfActualInProduction(EstimationCriteria criteria, Sort sort) {
        List<ProductionItemDTO> productionItemDTOs = findEstimationsByCriteria(criteria, sort).stream()
            .map(ProductionItemDTO::new).collect(Collectors.toList());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            productionExcelService.createExcelFile(productionItemDTOs, byteArrayOutputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new CustomParameterizedException("error.canNotCreateFile", e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();

    }

    @Transactional(readOnly = true)
    public byte[] makeExcelOfFinishedProduction(EstimationCriteria criteria, Sort sort) {
        List<ProductionItemDeliveredDTO> productionItemDTOs = findEstimationsByCriteria(criteria, sort).stream()
            .map(ProductionItemDeliveredDTO::new).collect(Collectors.toList());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            finishedProductionExcelService.createExcelFile(productionItemDTOs, byteArrayOutputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new CustomParameterizedException("error.canNotCreateFile", e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();

    }

    private Page<Estimation> findEstimationsByCriteria(EstimationCriteria criteria, Pageable pageable) {
        restrictByLoggedClient(criteria);


        return estimationQueryService.findByCriteria(criteria, pageable);
    }

    private List<Estimation> findEstimationsByCriteria(EstimationCriteria criteria, Sort sort) {
        restrictByLoggedClient(criteria);
        return estimationQueryService.findByCriteria(criteria, sort);
    }

    private void restrictByLoggedClient(EstimationCriteria criteria) {
        User user = userService.getLoggedUser();
        Set<Long> clientsId = user.getClients().stream().map(Client::getId).collect(Collectors.toSet());
        if (!clientsId.isEmpty()) {
            log.debug("user clients ids: {}", clientsId);
            if (criteria.getClientId() == null) {
                criteria.setClientId(new LongFilter());
            }
            criteria.getClientId().setIn(new ArrayList<>(clientsId));

        }

    }


    @Transactional(readOnly = true)
    public Page<ProductionItemDeliveredDTO> showArchivalProductionByCriteriaAndClient(EstimationCriteria criteria, Pageable pageable) {

        Page<Estimation> estimationPage = findEstimationsByCriteria(criteria, pageable);
        return estimationPage.map(ProductionItemDeliveredDTO::new);
    }

    @Transactional
    public void moveProductionItemToArchive(Long productionItemId, String receiver) {
        Estimation estimation = estimationRepository.findOne(productionItemId);
        estimation.setReceiver(receiver);
        estimation.setFinished(true);
        estimation.setDeliveredAt(ZonedDateTime.now());
        boolean isOrderFinished = estimation.getOrder().getEstimations().stream()
            .allMatch(Estimation::getFinished);
        if (isOrderFinished) {
            estimation.getOrder().setOrderStatus(OrderStatus.FINISHED);
            Audit audit = new Audit()
                .order(estimation.getOrder())
                .operation(AuditedOperation.MARK_FINISHED)
                .createdAt(ZonedDateTime.now())
                .createdBy(userService.getLoggedUser());
            estimation.getOrder().getAudits().add(audit);

        }
    }


    @Transactional
    @Secured(AuthoritiesConstants.ADMIN)
    public void returnToTechnologyVerification(Long orderId) {

        Order order = this.orderRepository.findOne(orderId);
        if (order != null) {
            Audit audit = new Audit()
                .order(order)
                .createdAt(ZonedDateTime.now())
                .createdBy(userService.getLoggedUser());
            switch (order.getOrderType()) {
                case PRODUCTION:
                    order.setOrderStatus(OrderStatus.TECHNOLOGY_VERIFICATION);
                    audit.setOperation(AuditedOperation.RETURN_TO_TECHNOLOGY_VERIFICATION);
                    break;
                case EMERGENCY:
                    order.setOrderStatus(OrderStatus.TECHNOLOGY_CREATION);
                    audit.setOperation(AuditedOperation.RETURN_TO_TECHNOLOGY_CREATION);

                    break;
                default:
                    log.warn("Type: {} should not occured");

                    order.getAudits().add(audit);

            }
        }
    }


//    @Transactional
//    @Secured(AuthoritiesConstants.ADMIN)
//    public void returnToProduction(Long orderId) {
//
//        Order order = this.orderRepository.findOne(orderId);
//        if (order != null) {
//            Audit audit = new Audit()
//                .order(order)
//                .createdAt(ZonedDateTime.now())
//                .createdBy(userService.getLoggedUser())
//                .operation(AuditedOperation.RETURN_TO_PRODUCTION);
//
//            order.getAudits().add(audit);
//            order
//
//        }
//    }
    @Transactional
    @Secured(AuthoritiesConstants.ADMIN)
    public void returnToProduction(Long estimationId) {

        Estimation estimation = this.estimationRepository.findOne(estimationId);
        if (estimation != null) {

            estimation.setFinished(false);
            estimation.setReceiver(null);
            estimation.setDeliveredAt(null);

            if(estimation.getOrder().getOrderStatus()==OrderStatus.FINISHED){
                estimation.getOrder().setOrderStatus(OrderStatus.IN_PRODUCTION);
                Audit audit = new Audit()
                    .order(estimation.getOrder())
                    .createdAt(ZonedDateTime.now())
                    .createdBy(userService.getLoggedUser())
                    .operation(AuditedOperation.RETURN_TO_PRODUCTION);

                estimation.getOrder().getAudits().add(audit);
            }



        }
    }

}
