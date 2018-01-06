package eu.canpack.fip.bo.estimation;

import com.codahale.metrics.annotation.Timed;
import eu.canpack.fip.bo.estimation.dto.EstimationCriteria;
import eu.canpack.fip.bo.estimation.dto.EstimationDTO;
import eu.canpack.fip.bo.estimation.dto.EstimationShowDTO;
import eu.canpack.fip.bo.operation.dto.OperationDTO;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import eu.canpack.fip.web.rest.util.HeaderUtil;
import eu.canpack.fip.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Estimation.
 */
@RestController
@RequestMapping("/api")
public class EstimationResource {

    private static final String ENTITY_NAME = "estimation";
    private final Logger log = LoggerFactory.getLogger(EstimationResource.class);
    private final EstimationService estimationService;

    public EstimationResource(EstimationService estimationService) {
        this.estimationService = estimationService;
    }

    /**
     * POST  /estimations : Create a new estimation.
     *
     * @param estimationDTO the estimationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new estimationDTO, or with status 400 (Bad Request) if the estimation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/estimations")
    @Timed
    public ResponseEntity<EstimationDTO> createEstimation(@Valid @RequestBody EstimationDTO estimationDTO) throws URISyntaxException {
        log.debug("REST request to save Estimation : {}", estimationDTO);
        if (estimationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new estimation cannot already have an ID")).body(null);
        }
        EstimationDTO result = estimationService.save(estimationDTO);
        return ResponseEntity.created(new URI("/api/estimations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /estimations : Updates an existing estimation.
     *
     * @param estimationDTO the estimationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated estimationDTO,
     * or with status 400 (Bad Request) if the estimationDTO is not valid,
     * or with status 500 (Internal Server Error) if the estimationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/estimations")
    @Timed
    public ResponseEntity<EstimationDTO> updateEstimation(@Valid @RequestBody EstimationDTO estimationDTO) throws URISyntaxException {
        log.debug("REST request to update Estimation : {}", estimationDTO);
        if (estimationDTO.getId() == null) {
            return createEstimation(estimationDTO);
        }
        EstimationDTO result = estimationService.save(estimationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, estimationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /estimations : get all the estimations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of estimations in body
     */
    @GetMapping("/estimations")
    @Timed
    public ResponseEntity<List<EstimationDTO>> getAllEstimations(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Estimations");
        Page<EstimationDTO> page = estimationService.findAll(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/estimations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /estimations/:id : get the "id" estimation.
     *
     * @param id the id of the estimationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the estimationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/estimations/{id}")
    @Timed
    public ResponseEntity<EstimationDTO> getEstimation(@PathVariable Long id) {
        log.debug("REST request to get Estimation : {}", id);
        EstimationDTO estimationDTO = estimationService.findOne(id);

        estimationDTO.getOperations().sort(Comparator.comparing(OperationDTO::getSequenceNumber));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(estimationDTO));
    }

    /**
     * DELETE  /estimations/:id : delete the "id" estimation.
     *
     * @param id the id of the estimationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/estimations/{id}")
    @Timed
    public ResponseEntity<Void> deleteEstimation(@PathVariable Long id) {
        log.debug("REST request to delete Estimation : {}", id);
        estimationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/estimations?query=:query : search for the estimation corresponding
     * to the query.
     *
     * @param query    the query of the estimation search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/estimations")
    @Timed
    public ResponseEntity<List<EstimationDTO>> searchEstimations(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Estimations for query {}", query);
        Page<EstimationDTO> page = estimationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/estimations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/estimations/{id}/technology-card")
    public ResponseEntity<InputStreamResource> getTechnologyCard(@PathVariable(name = "id") Long estimationId) throws IOException {
        ByteArrayOutputStream os = estimationService.getTechnologyCard(estimationId);

        InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        os.close();
        inputStream.close();


        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "karta_obiegowa.pdf").contentType(MediaType.APPLICATION_PDF).body(inputStreamResource);

    }

    @GetMapping("/estimations/to-finish")
    public ResponseEntity<List<EstimationDTO>> findToFinish(@ApiParam Pageable pageable) throws IOException {

        Page<EstimationDTO> page = estimationService.findEstimationToFinish(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/estimations/to-finish");

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

    }

    @GetMapping("/estimations/inquiry-item-finder")
    public ResponseEntity<List<EstimationShowDTO>> findInquiryByCriteria(EstimationCriteria estimationCriteria,@ApiParam Pageable pageable){
        if(estimationCriteria.getOrderTypeFilter()==null){
            estimationCriteria.setOrderTypeFilter(new EstimationCriteria.OrderTypeFilter());
        }
        estimationCriteria.getOrderTypeFilter().setEquals(OrderType.ESTIMATION);
        Page<EstimationShowDTO> page = estimationService.getAllInquiriesByCriteriaAndClient(estimationCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/estimations/inquiry-item-finder");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

    }
    @GetMapping("/estimations/purchase-order-item-finder")
    public ResponseEntity<List<EstimationShowDTO>> findPurchaseOrderByCriteria(EstimationCriteria estimationCriteria,@ApiParam Pageable pageable){
        if(estimationCriteria.getOrderTypeFilter()==null){
            estimationCriteria.setOrderTypeFilter(new EstimationCriteria.OrderTypeFilter());
        }
        estimationCriteria.getOrderTypeFilter().setEquals(OrderType.PRODUCTION);
        Page<EstimationShowDTO> page = estimationService.getAllInquiriesByCriteriaAndClient(estimationCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/estimations/purchase-order-item-finder");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

    }
    @GetMapping("/estimations/emergency-order-item-finder")
    public ResponseEntity<List<EstimationShowDTO>> findEmergencyOrderByCriteria(EstimationCriteria estimationCriteria,@ApiParam Pageable pageable){
        if(estimationCriteria.getOrderTypeFilter()==null){
            estimationCriteria.setOrderTypeFilter(new EstimationCriteria.OrderTypeFilter());
        }
        estimationCriteria.getOrderTypeFilter().setEquals(OrderType.EMERGENCY);
        Page<EstimationShowDTO> page = estimationService.getAllInquiriesByCriteriaAndClient(estimationCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/estimations/emergency-order-item-finder");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

    }

    @PutMapping("/estimations/{id}/publishPrice")
    public ResponseEntity<Void> publishPrice(@RequestBody PublishedPriceDTO publishedPriceDTO, @PathVariable(name = "id") Long id){
        estimationService.publishPrice(id,publishedPriceDTO.isPublished());
        return ResponseEntity.ok().build();
    }

    private static class PublishedPriceDTO{


        private Boolean published;

        public Boolean isPublished() {
            return published;
        }

        public void setPublished(Boolean published) {
            this.published = published;
        }
    }

}
