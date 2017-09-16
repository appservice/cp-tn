package eu.canpack.fip.bo.commercialPart;

import com.codahale.metrics.annotation.Timed;
import eu.canpack.fip.web.rest.util.HeaderUtil;
import eu.canpack.fip.web.rest.util.PaginationUtil;
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

/**
 * REST controller for managing CommercialPart.
 */
@RestController
@RequestMapping("/api")
public class CommercialPartResource {

    private final Logger log = LoggerFactory.getLogger(CommercialPartResource.class);

    private static final String ENTITY_NAME = "commercialPart";

    private final CommercialPartService commercialPartService;

    public CommercialPartResource(CommercialPartService commercialPartService) {
        this.commercialPartService = commercialPartService;
    }

    /**
     * POST  /commercial-parts : Create a new commercialPart.
     *
     * @param commercialPartDTO the commercialPartDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commercialPartDTO, or with status 400 (Bad Request) if the commercialPart has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commercial-parts")
    @Timed
    public ResponseEntity<CommercialPartDTO> createCommercialPart(@Valid @RequestBody CommercialPartDTO commercialPartDTO) throws URISyntaxException {
        log.debug("REST request to save CommercialPart : {}", commercialPartDTO);
        if (commercialPartDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new commercialPart cannot already have an ID")).body(null);
        }
        CommercialPartDTO result = commercialPartService.save(commercialPartDTO);
        return ResponseEntity.created(new URI("/api/commercial-parts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commercial-parts : Updates an existing commercialPart.
     *
     * @param commercialPartDTO the commercialPartDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commercialPartDTO,
     * or with status 400 (Bad Request) if the commercialPartDTO is not valid,
     * or with status 500 (Internal Server Error) if the commercialPartDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/commercial-parts")
    @Timed
    public ResponseEntity<CommercialPartDTO> updateCommercialPart(@Valid @RequestBody CommercialPartDTO commercialPartDTO) throws URISyntaxException {
        log.debug("REST request to update CommercialPart : {}", commercialPartDTO);
        if (commercialPartDTO.getId() == null) {
            return createCommercialPart(commercialPartDTO);
        }
        CommercialPartDTO result = commercialPartService.save(commercialPartDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commercialPartDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commercial-parts : get all the commercialParts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commercialParts in body
     */
    @GetMapping("/commercial-parts")
    @Timed
    public ResponseEntity<List<CommercialPartDTO>> getAllCommercialParts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CommercialParts");
        Page<CommercialPartDTO> page = commercialPartService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commercial-parts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commercial-parts/:id : get the "id" commercialPart.
     *
     * @param id the id of the commercialPartDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commercialPartDTO, or with status 404 (Not Found)
     */
    @GetMapping("/commercial-parts/{id}")
    @Timed
    public ResponseEntity<CommercialPartDTO> getCommercialPart(@PathVariable Long id) {
        log.debug("REST request to get CommercialPart : {}", id);
        CommercialPartDTO commercialPartDTO = commercialPartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commercialPartDTO));
    }

    /**
     * DELETE  /commercial-parts/:id : delete the "id" commercialPart.
     *
     * @param id the id of the commercialPartDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commercial-parts/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommercialPart(@PathVariable Long id) {
        log.debug("REST request to delete CommercialPart : {}", id);
        commercialPartService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/commercial-parts?query=:query : search for the commercialPart corresponding
     * to the query.
     *
     * @param query the query of the commercialPart search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/commercial-parts")
    @Timed
    public ResponseEntity<List<CommercialPartDTO>> searchCommercialParts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CommercialParts for query {}", query);
        Page<CommercialPartDTO> page = commercialPartService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/commercial-parts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
