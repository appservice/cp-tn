package eu.canpack.fip.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.canpack.fip.service.OperatorService;
import eu.canpack.fip.web.rest.errors.BadRequestAlertException;
import eu.canpack.fip.web.rest.util.HeaderUtil;
import eu.canpack.fip.web.rest.util.PaginationUtil;
import eu.canpack.fip.service.dto.OperatorDTO;
import eu.canpack.fip.service.dto.OperatorCriteria;
import eu.canpack.fip.service.OperatorQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Operator.
 */
@RestController
@RequestMapping("/api")
public class OperatorResource {

    private final Logger log = LoggerFactory.getLogger(OperatorResource.class);

    private static final String ENTITY_NAME = "operator";

    private final OperatorService operatorService;

    private final OperatorQueryService operatorQueryService;

    public OperatorResource(OperatorService operatorService, OperatorQueryService operatorQueryService) {
        this.operatorService = operatorService;
        this.operatorQueryService = operatorQueryService;
    }

    /**
     * POST  /operators : Create a new operator.
     *
     * @param operatorDTO the operatorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new operatorDTO, or with status 400 (Bad Request) if the operator has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/operators")
    @Timed
    public ResponseEntity<OperatorDTO> createOperator(@Valid @RequestBody OperatorDTO operatorDTO) throws URISyntaxException {
        log.debug("REST request to save Operator : {}", operatorDTO);
        if (operatorDTO.getId() != null) {
            throw new BadRequestAlertException("A new operator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OperatorDTO result = operatorService.save(operatorDTO);
        return ResponseEntity.created(new URI("/api/operators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /operators : Updates an existing operator.
     *
     * @param operatorDTO the operatorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated operatorDTO,
     * or with status 400 (Bad Request) if the operatorDTO is not valid,
     * or with status 500 (Internal Server Error) if the operatorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/operators")
    @Timed
    public ResponseEntity<OperatorDTO> updateOperator(@Valid @RequestBody OperatorDTO operatorDTO) throws URISyntaxException {
        log.debug("REST request to update Operator : {}", operatorDTO);
        if (operatorDTO.getId() == null) {
            return createOperator(operatorDTO);
        }
        OperatorDTO result = operatorService.save(operatorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, operatorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /operators : get all the operators.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of operators in body
     */
    @GetMapping("/operators")
    @Timed
    public ResponseEntity<List<OperatorDTO>> getAllOperators(OperatorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Operators by criteria: {}", criteria);
        Page<OperatorDTO> page = operatorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/operators");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /operators/:id : get the "id" operator.
     *
     * @param id the id of the operatorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the operatorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/operators/{id}")
    @Timed
    public ResponseEntity<OperatorDTO> getOperator(@PathVariable Long id) {
        log.debug("REST request to get Operator : {}", id);
        OperatorDTO operatorDTO = operatorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(operatorDTO));
    }

    /**
     * DELETE  /operators/:id : delete the "id" operator.
     *
     * @param id the id of the operatorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/operators/{id}")
    @Timed
    public ResponseEntity<Void> deleteOperator(@PathVariable Long id) {
        log.debug("REST request to delete Operator : {}", id);
        operatorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/operators?query=:query : search for the operator corresponding
     * to the query.
     *
     * @param query the query of the operator search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/operators")
    @Timed
    public ResponseEntity<List<OperatorDTO>> searchOperators(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Operators for query {}", query);
        Page<OperatorDTO> page = operatorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/operators");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
