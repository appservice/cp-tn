package eu.canpack.fip.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.canpack.fip.service.CooperationService;
import eu.canpack.fip.web.rest.util.HeaderUtil;
import eu.canpack.fip.web.rest.util.PaginationUtil;
import eu.canpack.fip.service.dto.CooperationDTO;
import eu.canpack.fip.service.dto.CooperationCriteria;
import eu.canpack.fip.service.CooperationQueryService;
import io.swagger.annotations.ApiParam;
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
 * REST controller for managing Cooperation.
 */
@RestController
@RequestMapping("/api")
public class CooperationResource {

    private final Logger log = LoggerFactory.getLogger(CooperationResource.class);

    private static final String ENTITY_NAME = "cooperation";

    private final CooperationService cooperationService;

    private final CooperationQueryService cooperationQueryService;

    public CooperationResource(CooperationService cooperationService, CooperationQueryService cooperationQueryService) {
        this.cooperationService = cooperationService;
        this.cooperationQueryService = cooperationQueryService;
    }

    /**
     * POST  /cooperation : Create a new cooperation.
     *
     * @param cooperationDTO the cooperationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cooperationDTO, or with status 400 (Bad Request) if the cooperation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cooperation")
    @Timed
    public ResponseEntity<CooperationDTO> createCooperation(@Valid @RequestBody CooperationDTO cooperationDTO) throws URISyntaxException {
        log.debug("REST request to save Cooperation : {}", cooperationDTO);
        if (cooperationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cooperation cannot already have an ID")).body(null);
        }
        CooperationDTO result = cooperationService.save(cooperationDTO);
        return ResponseEntity.created(new URI("/api/cooperation/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cooperation : Updates an existing cooperation.
     *
     * @param cooperationDTO the cooperationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cooperationDTO,
     * or with status 400 (Bad Request) if the cooperationDTO is not valid,
     * or with status 500 (Internal Server Error) if the cooperationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cooperation")
    @Timed
    public ResponseEntity<CooperationDTO> updateCooperation(@Valid @RequestBody CooperationDTO cooperationDTO) throws URISyntaxException {
        log.debug("REST request to update Cooperation : {}", cooperationDTO);
        if (cooperationDTO.getId() == null) {
            return createCooperation(cooperationDTO);
        }
        CooperationDTO result = cooperationService.save(cooperationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cooperationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cooperation : get all the cooperation.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of cooperation in body
     */
    @GetMapping("/cooperation")
    @Timed
    public ResponseEntity<List<CooperationDTO>> getAllCooperation(CooperationCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get Cooperation by criteria: {}", criteria);
        Page<CooperationDTO> page = cooperationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cooperation");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cooperation/:id : get the "id" cooperation.
     *
     * @param id the id of the cooperationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cooperationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cooperation/{id}")
    @Timed
    public ResponseEntity<CooperationDTO> getCooperation(@PathVariable Long id) {
        log.debug("REST request to get Cooperation : {}", id);
        CooperationDTO cooperationDTO = cooperationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cooperationDTO));
    }

    /**
     * DELETE  /cooperation/:id : delete the "id" cooperation.
     *
     * @param id the id of the cooperationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cooperation/{id}")
    @Timed
    public ResponseEntity<Void> deleteCooperation(@PathVariable Long id) {
        log.debug("REST request to delete Cooperation : {}", id);
        cooperationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cooperation?query=:query : search for the cooperation corresponding
     * to the query.
     *
     * @param query the query of the cooperation search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cooperation")
    @Timed
    public ResponseEntity<List<CooperationDTO>> searchCooperation(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Cooperation for query {}", query);
        Page<CooperationDTO> page = cooperationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cooperation");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
