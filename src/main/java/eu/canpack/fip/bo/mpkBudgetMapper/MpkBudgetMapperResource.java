package eu.canpack.fip.bo.mpkBudgetMapper;

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
 * REST controller for managing MpkBudgetMapper.
 */
@RestController
@RequestMapping("/api")
public class MpkBudgetMapperResource {

    private final Logger log = LoggerFactory.getLogger(MpkBudgetMapperResource.class);

    private static final String ENTITY_NAME = "mpkBudgetMapper";

    private final MpkBudgetMapperService mpkBudgetMapperService;

    public MpkBudgetMapperResource(MpkBudgetMapperService mpkBudgetMapperService) {
        this.mpkBudgetMapperService = mpkBudgetMapperService;
    }

    /**
     * POST  /mpk-budget-mappers : Create a new mpkBudgetMapper.
     *
     * @param mpkBudgetMapperDTO the mpkBudgetMapperDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mpkBudgetMapperDTO, or with status 400 (Bad Request) if the mpkBudgetMapper has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mpk-budget-mappers")
    @Timed
    public ResponseEntity<MpkBudgetMapperDTO> createMpkBudgetMapper(@Valid @RequestBody MpkBudgetMapperDTO mpkBudgetMapperDTO) throws URISyntaxException {
        log.debug("REST request to save MpkBudgetMapper : {}", mpkBudgetMapperDTO);
        if (mpkBudgetMapperDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new mpkBudgetMapper cannot already have an ID")).body(null);
        }
        MpkBudgetMapperDTO result = mpkBudgetMapperService.save(mpkBudgetMapperDTO);
        return ResponseEntity.created(new URI("/api/mpk-budget-mappers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mpk-budget-mappers : Updates an existing mpkBudgetMapper.
     *
     * @param mpkBudgetMapperDTO the mpkBudgetMapperDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mpkBudgetMapperDTO,
     * or with status 400 (Bad Request) if the mpkBudgetMapperDTO is not valid,
     * or with status 500 (Internal Server Error) if the mpkBudgetMapperDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mpk-budget-mappers")
    @Timed
    public ResponseEntity<MpkBudgetMapperDTO> updateMpkBudgetMapper(@Valid @RequestBody MpkBudgetMapperDTO mpkBudgetMapperDTO) throws URISyntaxException {
        log.debug("REST request to update MpkBudgetMapper : {}", mpkBudgetMapperDTO);
        if (mpkBudgetMapperDTO.getId() == null) {
            return createMpkBudgetMapper(mpkBudgetMapperDTO);
        }
        MpkBudgetMapperDTO result = mpkBudgetMapperService.save(mpkBudgetMapperDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mpkBudgetMapperDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mpk-budget-mappers : get all the mpkBudgetMappers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mpkBudgetMappers in body
     */
    @GetMapping("/mpk-budget-mappers")
    @Timed
    public ResponseEntity<List<MpkBudgetMapperDTO>> getAllMpkBudgetMappers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of MpkBudgetMappers");
        Page<MpkBudgetMapperDTO> page = mpkBudgetMapperService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mpk-budget-mappers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mpk-budget-mappers/:id : get the "id" mpkBudgetMapper.
     *
     * @param id the id of the mpkBudgetMapperDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mpkBudgetMapperDTO, or with status 404 (Not Found)
     */
    @GetMapping("/mpk-budget-mappers/{id}")
    @Timed
    public ResponseEntity<MpkBudgetMapperDTO> getMpkBudgetMapper(@PathVariable Long id) {
        log.debug("REST request to get MpkBudgetMapper : {}", id);
        MpkBudgetMapperDTO mpkBudgetMapperDTO = mpkBudgetMapperService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mpkBudgetMapperDTO));
    }

    /**
     * DELETE  /mpk-budget-mappers/:id : delete the "id" mpkBudgetMapper.
     *
     * @param id the id of the mpkBudgetMapperDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mpk-budget-mappers/{id}")
    @Timed
    public ResponseEntity<Void> deleteMpkBudgetMapper(@PathVariable Long id) {
        log.debug("REST request to delete MpkBudgetMapper : {}", id);
        mpkBudgetMapperService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/mpk-budget-mappers?query=:query : search for the mpkBudgetMapper corresponding
     * to the query.
     *
     * @param query the query of the mpkBudgetMapper search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/mpk-budget-mappers")
    @Timed
    public ResponseEntity<List<MpkBudgetMapperDTO>> searchMpkBudgetMappers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of MpkBudgetMappers for query {}", query);
        Page<MpkBudgetMapperDTO> page = mpkBudgetMapperService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/mpk-budget-mappers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
