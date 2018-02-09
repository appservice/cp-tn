package eu.canpack.fip.bo.order;

import com.codahale.metrics.annotation.Timed;
import eu.canpack.fip.bo.order.dto.*;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import eu.canpack.fip.bo.pdf.OfferPdfCreator;
import eu.canpack.fip.security.AuthoritiesConstants;
import eu.canpack.fip.web.rest.util.HeaderUtil;
import eu.canpack.fip.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Order.
 */
@RestController
@RequestMapping("/api")
public class OrderResource {

    private static final String ENTITY_NAME = "order";
    private final Logger log = LoggerFactory.getLogger(OrderResource.class);
    private final OrderService orderService;

    private final OrderMapper orderMapper;

    private final OfferPdfCreator offerPdfCreator;

    private final OrderQueryService orderQueryService;

    private final OrderExcelService orderExcelService;


    public OrderResource(OrderService orderService, OrderMapper orderMapper, OfferPdfCreator offerPdfCreator, OrderQueryService orderQueryService, OrderExcelService orderExcelService) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.offerPdfCreator = offerPdfCreator;
        this.orderQueryService = orderQueryService;
        this.orderExcelService = orderExcelService;
    }

    /**
     * POST  /orders : Create a new order.
     *
     * @param orderDTO the orderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderDTO, or with status 400 (Bad Request) if the order has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/orders")
    @Timed
    public ResponseEntity<OrderListDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) throws URISyntaxException {
        log.debug("REST request to save Order : {}", orderDTO);
        if (orderDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new order cannot already have an ID")).body(null);
        }
        OrderListDTO result = orderMapper.toDto(orderService.createOrder(orderDTO));
        return ResponseEntity.created(new URI("/api/orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orders : Updates an existing order.
     *
     * @param orderDTO the orderListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderListDTO,
     * or with status 400 (Bad Request) if the orderListDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderListDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/orders")
    @Timed
    public ResponseEntity<OrderListDTO> updateOrder(@Valid @RequestBody OrderDTO orderDTO) throws URISyntaxException {
        log.debug("REST request to update Order : {}", orderDTO);
        if (orderDTO.getId() == null) {
            return createOrder(orderDTO);
        }
        OrderListDTO result = orderService.updateOrder(orderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderDTO.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orders : Updates an existing order.
     *
     * @param orderDTO the orderListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderListDTO,
     * or with status 400 (Bad Request) if the orderListDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderListDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/orders/update-as-admin")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<OrderListDTO> updateOrderAsAdmin(@Valid @RequestBody OrderDTO orderDTO) throws URISyntaxException {
        log.debug("REST request to update Order as admin : {}", orderDTO);
        if (orderDTO.getId() == null) {
            return createOrder(orderDTO);
        }
        OrderListDTO result = orderService.updateOrderAsAdmin(orderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orders : get all the orders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/orders/inquiries")
    @Timed
    public ResponseEntity<List<OrderListDTO>> getAllInquiries(OrderCriteria orderCriteria, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Orders2");
//        if (orderCriteria == null) {
//            orderCriteria = new OrderCriteria();
//        }
        // orderCriteria.getOrderType().setEquals(OrderType.ESTIMATION);
        OrderCriteria.OrderTypeFilter orderTypeFilter = new OrderCriteria.OrderTypeFilter();
        orderTypeFilter.setEquals(OrderType.ESTIMATION);
        orderCriteria.setOrderType(orderTypeFilter);
        Page<OrderListDTO> page = orderQueryService.findByCriteriaAndClient(orderCriteria, pageable);//orderService.findAllByClientAndOrderType(pageable, OrderType.ESTIMATION);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders/inquiries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /orders : get all the orders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/orders/purchase-orders/to-excel")
    @Timed
    public byte[] getAllPurchaseOrdersInExcel(OrderCriteria orderCriteria, @ApiParam Pageable pageable) throws IOException {
        log.debug("REST request to get a page of Orders2");
//        if (orderCriteria == null) {
//            orderCriteria = new OrderCriteria();
//        }
        // orderCriteria.getOrderType().setEquals(OrderType.ESTIMATION);
        OrderCriteria.OrderTypeFilter orderTypeFilter = new OrderCriteria.OrderTypeFilter();
        orderTypeFilter.setEquals(OrderType.PRODUCTION);
        orderCriteria.setOrderType(orderTypeFilter);
        List<OrderListDTO> list = orderQueryService.findByCriteriaAndClient(orderCriteria);//orderService.findAllByClientAndOrderType(pageable, OrderType.ESTIMATION);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        orderExcelService.createExcelFile(list,OrderType.PRODUCTION, outputStream);

       // HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders/inquiries");

        return outputStream.toByteArray();//new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /orders : get all the orders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/orders/inquiries/to-excel")
    @Timed
    public byte[] getAllInquiriesInExcel(OrderCriteria orderCriteria, @ApiParam Pageable pageable) throws IOException {
        log.debug("REST request to get a page of Orders2");
//        if (orderCriteria == null) {
//            orderCriteria = new OrderCriteria();
//        }
        // orderCriteria.getOrderType().setEquals(OrderType.ESTIMATION);
        OrderCriteria.OrderTypeFilter orderTypeFilter = new OrderCriteria.OrderTypeFilter();
        orderTypeFilter.setEquals(OrderType.ESTIMATION);
        orderCriteria.setOrderType(orderTypeFilter);
        List<OrderListDTO> list = orderQueryService.findByCriteriaAndClient(orderCriteria);//orderService.findAllByClientAndOrderType(pageable, OrderType.ESTIMATION);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        orderExcelService.createExcelFile(list,OrderType.ESTIMATION, outputStream) ;

       // HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders/inquiries");

        return outputStream.toByteArray();//new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /orders : get all the orders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/orders/emergency/to-excel")
    @Timed
    public byte[] getAllEmergencyOrdersInExcel(OrderCriteria orderCriteria, @ApiParam Pageable pageable) throws IOException {
       // log.debug("REST request to get a page of Orders2");

        OrderCriteria.OrderTypeFilter orderTypeFilter = new OrderCriteria.OrderTypeFilter();
        orderTypeFilter.setEquals(OrderType.EMERGENCY);
        orderCriteria.setOrderType(orderTypeFilter);
        List<OrderListDTO> list = orderQueryService.findByCriteriaAndClient(orderCriteria);//orderService.findAllByClientAndOrderType(pageable, OrderType.ESTIMATION);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        orderExcelService.createExcelFile(list,OrderType.EMERGENCY, outputStream) ;


        return outputStream.toByteArray();//new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /orders : get all the orders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/orders/production")
    @Timed
    public ResponseEntity<List<OrderListDTO>> getAllProductionOrders(OrderCriteria orderCriteria, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Orders");
        OrderCriteria.OrderTypeFilter orderTypeFilter = new OrderCriteria.OrderTypeFilter();
        orderTypeFilter.setEquals(OrderType.PRODUCTION);

        orderCriteria.setOrderType(orderTypeFilter);


        Page<OrderListDTO> page = orderQueryService.findByCriteriaAndClient(orderCriteria, pageable);//findAllByClientAndOrderType(pageable, OrderType.PRODUCTION);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders/production");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /orders : get all the orders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/orders/emergency")
    @Timed
    public ResponseEntity<List<OrderListDTO>> getAllEmergencyOrders(OrderCriteria orderCriteria, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Emergency Orders");
        OrderCriteria.OrderTypeFilter orderTypeFilter = new OrderCriteria.OrderTypeFilter();
        orderTypeFilter.setEquals(OrderType.EMERGENCY);
        orderCriteria.setOrderType(orderTypeFilter);
        Page<OrderListDTO> page = orderQueryService.findByCriteriaAndClient(orderCriteria, pageable);//findAllByClientAndOrderType(pageable, OrderType.PRODUCTION);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders/emergency");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /orders : get all the orders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/orders/production-edit")
    @Timed
    public ResponseEntity<List<OrderListDTO>> getAllProductionOrdersForEdit(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Orders");


        Page<OrderListDTO> page = orderService.findOrderInProducitionForEdit(pageable);//findAllByClientAndOrderType(pageable, OrderType.PRODUCTION);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /orders : get all the orders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/orders/filtered")
    @Timed
    public ResponseEntity<List<OrderListDTO>> getAllOrdersFiltered(OrderCriteria orderCriteria, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Orders");
        Page<OrderListDTO> page = orderQueryService.findByCriteria(orderCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders/filtered");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /orders/:id : get the "id" order.
     *
     * @param id the id of the orderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/orders/{id}")
    @Timed
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        OrderDTO order = orderService.getOrder(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(order));
    }

    /**
     * GET  /orders/:id : get the "id" order.
     *
     * @param id the id of the orderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/inquiries/{id}")
    @Timed
    public ResponseEntity<OrderDTO> getInquiry(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        OrderDTO order = orderService.getInquiryForClient(id);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(order));
    }

    /**
     * GET  /orders/:id/estimated : get the "id" order.
     *
     * @param id the id of the orderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/orders/{id}/estimated")
    @Timed
    public ResponseEntity<OrderDTO> getOrderEstimated(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        OrderDTO order = orderService.getOrderEstimated(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(order));
    }

    /**
     * DELETE  /orders/:id : delete the "id" order.
     *
     * @param id the id of the orderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.debug("REST request to delete Order : {}", id);
        orderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/orders?query=:query : search for the order corresponding
     * to the query.
     *
     * @param query    the query of the order search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/orders")
    @Timed
    public ResponseEntity<List<OrderListDTO>> searchOrders(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Orders for query {}", query);
        Page<OrderListDTO> page = orderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /orders/simple-dto/:id : get the "id" order.
     *
     * @param id the id of the orderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/orders/simple-dto/{id}")
    @Timed
    public ResponseEntity<OrderSimpleDTO> getOrderSimpleDto(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        OrderSimpleDTO order = orderService.getSimpleOrderDtoData(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(order));
    }

    /**
     * GET  /orders/simple-dto/:id : get the "id" order.
     *
     * @param pageable the id of the orderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/orders/not-estimated/")
    @Timed
    public ResponseEntity<List<OrderListDTO>> findOrderToClaimEstimation(OrderCriteria orderCriteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get Orders to estimation : {}");

        Page<OrderListDTO> page = orderQueryService.findOrdersClaimToEstimation(orderCriteria,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/orders/not-estimated");

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

    }
//
//    /**
//     * GET  /orders/simple-dto/:id : get the "id" order.
//     *
//     * @param pageable the id of the orderDTO to retrieve
//     * @return the ResponseEntity with status 200 (OK) and with body the orderDTO, or with status 404 (Not Found)
//     */
//    @GetMapping("/orders/not-estimated/")
//    @Timed
//    public ResponseEntity<List<OrderListDTO>> findOrderToClaimEstimation(@ApiParam Pageable pageable) {
//        log.debug("REST request to get Orders to estimation : {}");
//
//        Page<OrderListDTO> page = orderService.findOrderToClaimEstimation(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/orders/not-estimated");
//
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//
//    }

    /**
     * GET  /orders/simple-dto/:id : get the "id" order.
     *
     * @param pageable the id of the orderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/orders/in-estimation")
    @Timed
    public ResponseEntity<List<OrderListDTO>> findOrderToEstimation(@ApiParam Pageable pageable) {
        log.debug("REST request to get Orders to estimation : {}");

        Page<OrderListDTO> page = orderService.findOrderToEstimationByUser(pageable).map(orderMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/orders/in-estimation");

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

    }

    @PutMapping("/orders/{id}/claim-to-estimator")
    @Timed
    public ResponseEntity<Void> claimToEstimator(@PathVariable Long id) {
        orderService.claimByEstimatior(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/orders/insert-sap-numbers")
    @Timed
    public ResponseEntity<OrderDTO> insertSapNumbers(@RequestBody OrderDTO orderDTO) {
        log.debug("orderDTO", orderDTO);
        OrderDTO response = orderService.insertSapNumbers(orderDTO);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/orders/{id}/move-to-production")
    @Timed
    public ResponseEntity<Void> moveOrderToProduction(@PathVariable Long id) {

        log.debug("order form moving to production id {}", id);
        orderService.moveOrderToProduction(id);

        return ResponseEntity.ok().build();
    }


    @PostMapping("/orders/create-pdf-offer")
    public ResponseEntity<InputStreamResource> getTechnologyCard(@RequestBody OrderDTO orderDTO) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        offerPdfCreator.createPdf(orderDTO, os);

        InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        os.close();
        inputStream.close();


        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "oferta.pdf")
            .contentType(MediaType.APPLICATION_PDF).body(inputStreamResource);

    }

    @PutMapping("/orders/{id}/move-to-archive")
    @Timed
    public ResponseEntity<Void> moveOrderToArchive(@PathVariable Long id) {
        log.debug("REST request to move order to archive: {}", id);


        orderService.moveOrderToArchive(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/orders/{id}/clone")
    @Timed
    public ResponseEntity<Void> cloneOrder(@PathVariable Long id) {
        log.debug("REST request to clone order with id: {}", id);


        orderService.cloneOrder(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/purchase-orders")
    @Timed
    public ResponseEntity<OrderListDTO> createPurchaseOrder(@RequestBody OrderDTO orderDTO) {
        log.debug("REST request to  createPurchaseOrder by with OrderDTO : {}", orderDTO);

        Order order = orderService.createPurchaseOrder(orderDTO);
        OrderListDTO response = orderMapper.toDto(order);
        return ResponseEntity.created(URI.create("/orders/" + order.getId())).body(response);
    }


    /**
     * GET  /orders : get all the orders.
     * ez
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/orders/archived")
    @Timed
    public ResponseEntity<List<OrderListDTO>> getAllArchivedOrder(OrderCriteria orderCriteria, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Orders with criteria {}", orderCriteria);
        if (orderCriteria.getOrderStatus() == null) {
            orderCriteria.setOrderStatus(new OrderCriteria.OrderStatusFilter());
        }
        List<OrderStatus> orderStatusList = new ArrayList<>();
        orderStatusList.add(OrderStatus.SENT_OFFER_TO_CLIENT);
       // orderStatusList.add(OrderStatus.CREATED_PURCHASE_ORDER);
        orderCriteria.getOrderStatus().setIn(orderStatusList);
        Page<OrderListDTO> page = orderQueryService.findByCriteria(orderCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders/filtered");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @PutMapping("/orders/{id}/add-offer-remarks")
    @Timed
    public ResponseEntity<Void> addOfferRemarks(@PathVariable Long id, @RequestBody String text) {
        log.debug(" add offer remarks for order with id {}, text: {}",id,text);
        orderService.saveOfferRemarks(id, text);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/orders/{id}/remove-assigned-estimator")
    @Timed
    public ResponseEntity<Void> removeAssignedEstimator(@PathVariable Long id){
        orderService.removeAssingedEstimator(id);

        return ResponseEntity.ok().build();
    }
}
