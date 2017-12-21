package eu.canpack.fip.bo.order;

import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.client.Client_;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.estimation.Estimation_;
import eu.canpack.fip.bo.order.dto.OrderCriteria;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import eu.canpack.fip.bo.order.dto.OrderListDTO;
import eu.canpack.fip.bo.order.dto.OrderMapper;
import eu.canpack.fip.domain.User;
import eu.canpack.fip.domain.User_;
import eu.canpack.fip.service.UserService;
import io.github.jhipster.service.QueryService;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for executing complex queries for Order entities in the database.
 * The main input is a {@link OrderCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link OrderDTO} or a {@link Page} of {%link OrderDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class OrderQueryService extends QueryService<Order> {

    private final Logger log = LoggerFactory.getLogger(OrderQueryService.class);


    private final OrderRepository OrderRepository;

    private final eu.canpack.fip.bo.order.dto.OrderMapper OrderMapper;

    private final UserService userService;

    public OrderQueryService(OrderRepository OrderRepository, OrderMapper OrderMapper, UserService userService) {
        this.OrderRepository = OrderRepository;
        this.OrderMapper = OrderMapper;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {%link OrderDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderListDTO> findByCriteria(OrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Order> specification = createSpecification(criteria);
        return OrderMapper.toDto(OrderRepository.findAll(specification));
    }
//    /**
//     * Return a {@link List} of {%link OrderDTO} which matches the criteria from the database
//     *
//     * @param criteria The object which holds all the filters, which the entities should match.
//     * @return the matching entities.
//     */
//    @Transactional(readOnly = true)
//    public List<OrderListDTO> findByCriteriaNotPageable(OrderCriteria criteria,Pageable page) {
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
    public Page<OrderListDTO> findByCriteria(OrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Order> specification = createSpecification(criteria);
        final Page<Order> result = OrderRepository.findAll(specification, page);
        return result.map(OrderMapper::toDto);
    }

    /**
     * Function to convert OrderCriteria to a {@link Specifications}
     */
    private Specifications<Order> createSpecification(OrderCriteria criteria) {
        Specifications<Order> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), eu.canpack.fip.bo.order.Order_.id));
            }
            if (criteria.getReferenceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReferenceNumber(), eu.canpack.fip.bo.order.Order_.referenceNumber));
            }
            if (criteria.getInternalNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInternalNumber(), eu.canpack.fip.bo.order.Order_.internalNumber));
            }

            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), eu.canpack.fip.bo.order.Order_.createdAt));
            }

            if (criteria.getOrderStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderStatus(), eu.canpack.fip.bo.order.Order_.orderStatus));
            }
            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreatedById(), eu.canpack.fip.bo.order.Order_.createdBy, User_.id));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getClientId(), eu.canpack.fip.bo.order.Order_.client, Client_.id));
            }
//            if (criteria.getClientName() != null) {
//                specification = specification.and(buildReferringEntitySpecification(criteria.getClientName(), eu.canpack.fip.bo.order.Order_.client, Client_.name));
//            }
            if (criteria.getCreatedByLastName() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreatedByLastName(), eu.canpack.fip.bo.order.Order_.createdBy, User_.lastName));
            }
            if (criteria.getCreatedByFirstName() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreatedByFirstName(), eu.canpack.fip.bo.order.Order_.createdBy, User_.firstName));
            }
            if (criteria.getOrderType() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderType(), eu.canpack.fip.bo.order.Order_.orderType));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), eu.canpack.fip.bo.order.Order_.name));
            }
            if (criteria.getClientName() != null) {
//                log.debug("drawingNumber {}",criteria.getDrawingNumber().getContains());
                Specification<Order> spec = (root, query, builder) -> {
                    Join<Order, Client> joinToClient = root.join(eu.canpack.fip.bo.order.Order_.client, JoinType.INNER);
                    return builder.or(builder.like(builder.upper(joinToClient.get(Client_.name)), "%" + criteria.getClientName().getContains().trim().toUpperCase() + "%"),
                                      builder.like(builder.upper(joinToClient.get(Client_.shortcut)), "%" + criteria.getClientName().getContains().trim().toUpperCase() + "%"));


                };
                specification = specification.and(spec);

            }
            //todo
            if (criteria.getDrawingNumber() != null) {
//                log.debug("drawingNumber {}",criteria.getDrawingNumber().getContains());
                Specification<Order> spec = (root, query, builder) -> {
                    Join<Order, Estimation> joinToEstimation = root.join(eu.canpack.fip.bo.order.Order_.estimations, JoinType.INNER);

//                    return builder.and(builder.isMember( builder.upper(joinToEstimation.get(Estimation_.itemNumber)), "%" + criteria.getDrawingNumber().getContains().trim().toUpperCase() + "%");
//                        ;
//                    Expression<List<Estimation>> estimations = root.get(eu.canpack.fip.bo.order.Order_.estimations);
//                    return builder.in(criteria.getDrawingNumber().getContains(),estimations.)
                    query.distinct(true);
                    return builder.like(builder.upper(joinToEstimation.get(Estimation_.itemNumber))
                        , "%" + criteria.getDrawingNumber().getContains().toUpperCase() + "%");

                };

                specification = specification.and(spec);

            }
        }
        return specification;
    }


    /**
     * Return a {@link Page} of {%link OrderDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderListDTO> findByCriteriaAndClient(OrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);

        User user = userService.getLoggedUser();
        Client client = user.getClient();
        if (client != null) {
            log.debug("user client: {}", client);
            if (criteria.getClientId() == null) {
                criteria.setClientId(new LongFilter());
            }
            criteria.getClientId().setEquals(client.getId());

        }
        final Specifications<Order> specification = createSpecification(criteria);

        final Page<Order> result = OrderRepository.findAll(specification, page);

        return result.map(OrderMapper::toDto);
    }

    /**
     * Return a {@link Page} of {%link OrderDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
//     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderListDTO> findByCriteriaAndClient(OrderCriteria criteria) {
        log.debug("find by criteria : {}, page: {}", criteria);

        User user = userService.getLoggedUser();
        Client client = user.getClient();
        if (client != null) {
            log.debug("user client: {}", client);
            if (criteria.getClientId() == null) {
                criteria.setClientId(new LongFilter());
            }
            criteria.getClientId().setEquals(client.getId());

        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        final Specifications<Order> specification = createSpecification(criteria);

        final List<Order> result = OrderRepository.findAll(specification,sort);

        return result.stream().map(OrderMapper::toDto).collect(Collectors.toList());
    }
    /**
     * Return a {@link Page} of {%link OrderDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
//     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderListDTO> findByCriteriaAndClient(OrderCriteria criteria, Sort sort) {
        log.debug("find by criteria : {}, page: {}", criteria);

        User user = userService.getLoggedUser();
        Client client = user.getClient();
        if (client != null) {
            log.debug("user client: {}", client);
            if (criteria.getClientId() == null) {
                criteria.setClientId(new LongFilter());
            }
            criteria.getClientId().setEquals(client.getId());

        }
        final Specifications<Order> specification = createSpecification(criteria);

        final List<Order> result = OrderRepository.findAll(specification,sort);

        return result.stream().map(OrderMapper::toDto).collect(Collectors.toList());
    }


}
