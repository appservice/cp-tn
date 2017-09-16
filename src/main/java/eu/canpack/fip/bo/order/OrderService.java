package eu.canpack.fip.bo.order;

import eu.canpack.fip.bo.attachment.Attachment;
import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.estimation.EstimationCreateDTO;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import eu.canpack.fip.bo.order.dto.OrderListDTO;
import eu.canpack.fip.bo.order.dto.OrderSimpleDTO;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.domain.User;
import eu.canpack.fip.bo.attachment.AttachmentRepository;
import eu.canpack.fip.bo.estimation.EstimationRepository;
import eu.canpack.fip.repository.UserRepository;
import eu.canpack.fip.repository.search.OrderSearchRepository;
import eu.canpack.fip.security.SecurityUtils;
import eu.canpack.fip.service.UserService;
import eu.canpack.fip.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Order.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final OrderSearchRepository orderSearchRepository;

    private final UserRepository userRepository;

    private final AttachmentRepository attachmentRepository;
    private final EstimationRepository estimationRepository;


    private final UserService userService;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, OrderSearchRepository orderSearchRepository, UserRepository userRepository, AttachmentRepository attachmentRepository, EstimationRepository estimationRepository,  UserService userService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderSearchRepository = orderSearchRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.estimationRepository = estimationRepository;
        this.userService = userService;
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
        orderSearchRepository.save(order);
        return result;
    }


    public Order createOrder(OrderDTO orderDTO) {
        log.debug("Request to create Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);

        List<Estimation> estimations = orderDTO.getEstimations().stream().map(estDTO -> {
            Estimation estimation = new Estimation()
                .order(order)
                .description(estDTO.getDescription())
                .neededRealizationDate(estDTO.getNeededRealizationDate())
                .amount(estDTO.getAmount());

            estimation.setId(estDTO.getId());


            Drawing drawing = new Drawing();
            drawing.setEstimation(estimation);
            drawing.setNumber(estDTO.getDrawing().getNumber());
            drawing.setName(estDTO.getDescription());
            drawing.setId(estDTO.getDrawing().getId());
            List<Attachment> attahcments = estDTO.getDrawing().getAttachments().stream()
                .map(a -> attachmentRepository.findOne(a.getId())).collect(Collectors.toList());
            drawing.setAttachments(attahcments);
            estimation.setDrawing(drawing);

            return estimation;

        }).collect(Collectors.toList());
        order.setEstimations(estimations);

        Optional<User> currentUserOpt = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (currentUserOpt.isPresent()) {
            order.setCreatedBy(currentUserOpt.get());
        } else {
            throw new CustomParameterizedException("Nie odnaleziono użytkownika w bazie danych");
        }
        order.createdAt(ZonedDateTime.now());
        prepareDocumentNumber(order);

//        order.setOrderStatus(OrderStatus.WORKING_COPY);
        log.debug("Order to save: {}", order);

        return orderRepository.save(order);

    }

    public OrderListDTO updateOrder(OrderDTO orderDTO) {
        log.debug("Request to update Order : {}", orderDTO);
        Order orderFromDb = orderRepository.findOne(orderDTO.getId());
        Order order = orderMapper.toEntity(orderDTO);

        List<Estimation> newEstimations = orderDTO.getEstimations().stream()
            .filter(es -> es.getId() == null)
            .map(estDTO -> {
                Estimation estimation = new Estimation()
                    .order(order)
                    .description(estDTO.getDescription())
                    .neededRealizationDate(estDTO.getNeededRealizationDate())
                    .amount(estDTO.getAmount());

                estimation.setId(estDTO.getId());


                Drawing drawing = new Drawing();
                drawing.setEstimation(estimation);
                drawing.setNumber(estDTO.getDrawing().getNumber());
                drawing.setId(estDTO.getDrawing().getId());
                List<Attachment> attahcments = estDTO.getDrawing().getAttachments().stream()
                    .map(a -> attachmentRepository.findOne(a.getId())).collect(Collectors.toList());
                drawing.setAttachments(attahcments);
                estimation.setDrawing(drawing);

                return estimation;

            }).collect(Collectors.toList());

        List<Estimation> oldEstimation = orderDTO.getEstimations().stream()
            .filter(est -> est.getId() != null)
            .map(estDTO -> {
                Estimation estimation = estimationRepository.findOne(estDTO.getId());
                estimation.setDescription(estDTO.getDescription());
                estimation.setAmount(estDTO.getAmount());
                estimation.setNeededRealizationDate(estDTO.getNeededRealizationDate());
                estimation.getDrawing().setNumber(estDTO.getDrawing().getNumber());
                estimation.setDescription(estDTO.getDescription());
                List<Attachment> attachments = estDTO.getDrawing().getAttachments().stream()
                    .map(a -> attachmentRepository.findOne(a.getId())).collect(Collectors.toList());
                estimation.getDrawing().setAttachments(attachments);

                return estimation;
            }).collect(Collectors.toList());
        order.setEstimations(oldEstimation);
        order.getEstimations().addAll(newEstimations);


        Optional<User> currentUserOpt = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (currentUserOpt.isPresent()) {
            order.setCreatedBy(currentUserOpt.get());
        } else {
            throw new CustomParameterizedException("Nie odnaleziono użytkownika w bazie danych");
        }
        order.createdAt(ZonedDateTime.now());
//        prepareDocumentNumber(order);

//        order.setOrderStatus(OrderStatus.WORKING_COPY);
        log.debug("Order to save: {}", order);

        return orderMapper.toDto(orderRepository.save(order));

    }


    /**
     * Get all the orders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        User user = userService.getLoggedUser();
        Client client = user.getClient();
        if (client != null) {
            return orderRepository.findAllByClient(client, pageable).map(orderMapper::toDto);
        }
        return orderRepository.findAll(pageable)
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

        order.getEstimations().size();

        List<EstimationCreateDTO> estimationCreateDTOS = order.getEstimations().stream()
            .map(EstimationCreateDTO::new)
            .collect(Collectors.toList());
        orderDTO.setEstimations(estimationCreateDTOS);
        return orderDTO;

    }

    /**
     * Delete the  order by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.delete(id);
        orderSearchRepository.delete(id);
    }

    /**
     * Search for the order corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderListDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Orders for query {}", query);
        Page<Order> result = orderSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(orderMapper::toDto);
    }

    private void prepareDocumentNumber(Order order) {
        int year = LocalDate.now().getYear();
        int number = orderRepository.getDocNumber(year) + 1;
        order.setYear(year);
        order.setNumber(number);

        order.setInternalNumber(number + "/" + year + "/" + order.getOrderType().getShortcut());

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
    }

    Page<Order> findOrderToEstimationByUser(Pageable pageable){
        User logedUser = userService.getLoggedUser();
        return orderRepository.findOrderToEstimationByUser(logedUser.getId(),pageable);
    }

    void moveOrderToArchive(Long id){
        Order order = orderRepository.findOne(id);
        order.setOrderStatus(OrderStatus.SENT_OFFER_TO_CLIENT);
        orderRepository.save(order);
    }
}