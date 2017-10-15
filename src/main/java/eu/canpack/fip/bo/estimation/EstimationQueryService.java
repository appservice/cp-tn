package eu.canpack.fip.bo.estimation;

import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.client.Client_;
import eu.canpack.fip.bo.estimation.dto.EstimationCriteria;
import eu.canpack.fip.bo.estimation.dto.EstimationShowDTO;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.OrderMapper;
import eu.canpack.fip.bo.order.OrderRepository;
import eu.canpack.fip.bo.order.Order_;
import eu.canpack.fip.bo.order.dto.OrderCriteria;
import eu.canpack.fip.service.UserService;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for Order entities in the database.
 * The main input is a {@link OrderCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link OrderDTO} or a {@link Page} of {%link OrderDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class EstimationQueryService extends QueryService<Estimation> {

    private final Logger log = LoggerFactory.getLogger(EstimationQueryService.class);


    private final EstimationRepository estimationRepository;


    public EstimationQueryService(EstimationRepository estimationRepository) {
        this.estimationRepository = estimationRepository;

    }

//    /**
//     * Return a {@link List} of {%link OrderDTO} which matches the criteria from the database
//     *
//     * @param criteria The object which holds all the filters, which the entities should match.
//     * @return the matching entities.
//     */
//    @Transactional(readOnly = true)
//    public List<OrderListDTO> findByCriteria(OrderCriteria criteria) {
//        log.debug("find by criteria : {}", criteria);
//        final Specifications<Order> specification = createSpecification(criteria);
//        return OrderMapper.toDto(OrderRepository.findAll(specification));
//    }

    /**
     * Return a {@link Page} of {%link OrderDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EstimationShowDTO> findByCriteria(EstimationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Estimation> specification = createSpecification(criteria);
        final Page<Estimation> result = estimationRepository.findAll(specification, page);
        return result.map(EstimationShowDTO::new);
    }

    /**
     * Function to convert OrderCriteria to a {@link Specifications}
     */
    private Specifications<Estimation> createSpecification(EstimationCriteria criteria) {
        Specifications<Estimation> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getItemName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemName(), eu.canpack.fip.bo.estimation.Estimation_.itemName));
            }
            if (criteria.getItemNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemNumber(), eu.canpack.fip.bo.estimation.Estimation_.itemNumber));
            }

            if (criteria.getClientName() != null) {
//                log.debug("drawingNumber {}",criteria.getDrawingNumber().getContains());
                Specification<Estimation> spec = (root, query, builder) -> {
                    Join<Estimation, Order> joinToOrder = root.join((eu.canpack.fip.bo.estimation.Estimation_.order), JoinType.INNER);
                    Join<Order, Client> joinToClient = joinToOrder.join(eu.canpack.fip.bo.order.Order_.client, JoinType.INNER);
                    return builder.or(builder.like(joinToClient.get(Client_.name), "%" + criteria.getClientName().getContains().trim() + "%"),
                                      builder.like(joinToClient.get(Client_.shortcut), "%" + criteria.getClientName().getContains().trim() + "%"));


                };
                specification = specification.and(spec);

            }
            if (criteria.getOrderTypeFilter() != null) {
                Specification<Estimation> spec = (root, query, builder) -> {
                    Join<Estimation, Order> joinToOrder = root.join((eu.canpack.fip.bo.estimation.Estimation_.order), JoinType.INNER);
                    return builder.equal(joinToOrder.get(Order_.orderType), criteria.getOrderTypeFilter());

                };
                specification = specification.and(spec);

            }
        }
        return specification;
    }


//    /**
//     * Return a {@link Page} of {%link OrderDTO} which matches the criteria from the database
//     *
//     * @param criteria The object which holds all the filters, which the entities should match.
//     * @param page     The page, which should be returned.
//     * @return the matching entities.
//     */
//    @Transactional(readOnly = true)
//    public Page<OrderListDTO> findByCriteriaAndClient(OrderCriteria criteria, Pageable page) {
//        log.debug("find by criteria : {}, page: {}", criteria, page);
//
//        User user = userService.getLoggedUser();
//        Client client = user.getClient();
//        if (client != null) {
//            log.debug("user client: {}",client);
//            if(criteria.getClientId()==null){
//                criteria.setClientId(new LongFilter());
//            }
//            criteria.getClientId().setEquals(client.getId());
//
//        }
//        final Specifications<Order> specification = createSpecification(criteria);
//
//        final Page<Order> result = OrderRepository.findAll(specification, page);
//        return result.map(OrderMapper::toDto);
//    }


}