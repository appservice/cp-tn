package eu.canpack.fip.bo.production;

import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.estimation.EstimationQueryService;
import eu.canpack.fip.bo.estimation.EstimationRepository;
import eu.canpack.fip.bo.estimation.dto.EstimationCriteria;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.domain.User;
import eu.canpack.fip.service.UserService;
import io.github.jhipster.service.filter.LongFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * CP S.A.
 * Created by lukasz.mochel on 29.10.2017.
 */
@Service
@Transactional
public class ProductionService {

    private final UserService userService;

    private final EstimationRepository estimationRepository;

    private final EstimationQueryService estimationQueryService;

    public ProductionService(UserService userService, EstimationRepository estimationRepository, EstimationQueryService estimationQueryService) {
        this.userService = userService;
        this.estimationRepository = estimationRepository;
        this.estimationQueryService = estimationQueryService;
    }

    public Page<ProductionItemDTO> showActualProduction(Pageable pageable){
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

    private Page<Estimation> findEstimationsByCriteria(EstimationCriteria criteria, Pageable pageable) {
        User user = userService.getLoggedUser();
        Client client = user.getClient();

        if (client != null) {
            if (criteria.getClientId() == null) {
                criteria.setClientId(new LongFilter());
            }
            criteria.getClientId().setEquals(client.getId());
        }


        return estimationQueryService.findByCriteria(criteria, pageable);
    }


    @Transactional(readOnly = true)
    public Page<ProductionItemDeliveredDTO> showArchivalProductionByCriteriaAndClient(EstimationCriteria criteria, Pageable pageable) {

        Page<Estimation> estimationPage = findEstimationsByCriteria(criteria, pageable);
        return estimationPage.map(ProductionItemDeliveredDTO::new);
    }

    @Transactional
    public void moveProductionItemToArchive(Long productionItemId,String receiver){
        Estimation estimation = estimationRepository.findOne(productionItemId);
        estimation.setReceiver(receiver);
        estimation.setFinished(true);
        estimation.setDeliveredAt(ZonedDateTime.now());
       boolean isOrderFinished= estimation.getOrder().getEstimations().stream()
            .allMatch(Estimation::getFinished);
       if(isOrderFinished){
           estimation.getOrder().setOrderStatus(OrderStatus.FINISHED);

       }
    }
}
