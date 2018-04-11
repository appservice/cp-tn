package eu.canpack.fip.bo.technologyCard;

import com.codahale.metrics.annotation.Timed;
import eu.canpack.fip.bo.estimation.dto.EstimationDTO;
import eu.canpack.fip.bo.technologyCard.mapper.TechnologyCardDTO;
import eu.canpack.fip.bo.technologyCard.mapper.TechnologyCardListDTO;
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
 * REST controller for managing TechnologyCard.
 */
@RestController
@RequestMapping("/api")
public class TechnologyCardResource {

    private final Logger log = LoggerFactory.getLogger(TechnologyCardResource.class);

    private static final String ENTITY_NAME = "technologyCard";

    private final TechnologyCardService technologyCardService;

    private final TechnologyCardQueryService technologyCardQueryService;

    public TechnologyCardResource(TechnologyCardService technologyCardService, TechnologyCardQueryService technologyCardQueryService) {
        this.technologyCardService = technologyCardService;
        this.technologyCardQueryService = technologyCardQueryService;
    }

    /**
     * POST  /technology-cards : Create a new technologyCard.
     *
     * @param technologyCardDTO the technologyCardListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new technologyCardListDTO, or with status 400 (Bad Request) if the technologyCard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/technology-cards")
    @Timed
    public ResponseEntity<TechnologyCardDTO> createTechnologyCard(@Valid @RequestBody TechnologyCardDTO technologyCardDTO) throws URISyntaxException {
        log.debug("REST request to save TechnologyCard : {}", technologyCardDTO);
        if (technologyCardDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new technologyCard cannot already have an ID")).body(null);
        }
        TechnologyCardDTO result = technologyCardService.save(technologyCardDTO);
        return ResponseEntity.created(new URI("/api/technology-cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /technology-cards : Updates an existing technologyCard.
     *
     * @param technologyCardListDTO the technologyCardListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated technologyCardListDTO,
     * or with status 400 (Bad Request) if the technologyCardListDTO is not valid,
     * or with status 500 (Internal Server Error) if the technologyCardListDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/technology-cards")
    @Timed
    public ResponseEntity<TechnologyCardDTO> updateTechnologyCard(@Valid @RequestBody TechnologyCardDTO technologyCardListDTO) throws URISyntaxException {
        log.debug("REST request to update TechnologyCard : {}", technologyCardListDTO);
        if (technologyCardListDTO.getId() == null) {
            return createTechnologyCard(technologyCardListDTO);
        }
        TechnologyCardDTO result = technologyCardService.save(technologyCardListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, technologyCardListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /technology-cards : get all the technologyCards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of technologyCards in body
     */
    @GetMapping("/technology-cards")
    @Timed
    public ResponseEntity<List<TechnologyCardListDTO>> getAllTechnologyCards(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TechnologyCards");
        Page<TechnologyCardListDTO> page = technologyCardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/technology-cards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /technology-cards : get all the technologyCards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of technologyCards in body
     */
    @GetMapping("/technology-cards/filtered")
    @Timed
    public ResponseEntity<List<TechnologyCardListDTO>> getAllTechnologyCardsFiltered(TechnologyCardCriteria technologyCardCriteria, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TechnologyCards");
        Page<TechnologyCardListDTO> page = technologyCardQueryService.findByCriteria(technologyCardCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/technology-cards/filtered");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /technology-cards/:id : get the "id" technologyCard.
     *
     * @param id the id of the technologyCardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the technologyCardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/technology-cards/{id}")
    @Timed
    public ResponseEntity<TechnologyCardDTO> getTechnologyCard(@PathVariable Long id) {
        log.debug("REST request to get TechnologyCard : {}", id);
        TechnologyCardDTO technologyCardDTO = technologyCardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(technologyCardDTO));
    }

    /**
     * DELETE  /technology-cards/:id : delete the "id" technologyCard.
     *
     * @param id the id of the technologyCardDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/technology-cards/{id}")
    @Timed
    public ResponseEntity<Void> deleteTechnologyCard(@PathVariable Long id) {
        log.debug("REST request to delete TechnologyCard : {}", id);
        technologyCardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

//    /**
//     * SEARCH  /_search/technology-cards?query=:query : search for the technologyCard corresponding
//     * to the query.
//     *
//     * @param query the query of the technologyCard search
//     * @param pageable the pagination information
//     * @return the result of the search
//     */
//    @GetMapping("/_search/technology-cards")
//    @Timed
//    public ResponseEntity<List<TechnologyCardListDTO>> searchTechnologyCards(@RequestParam String query, @ApiParam Pageable pageable) {
//        log.debug("REST request to search for a page of TechnologyCards for query {}", query);
//        Page<TechnologyCardListDTO> page = technologyCardService.search(query, pageable);
//        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/technology-cards");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//    }

    @PostMapping("/technology-cards/created-from-estimation")
    @Timed
    public ResponseEntity<TechnologyCard> createFromEstimation(@RequestBody EstimationDTO estimationDTO) throws URISyntaxException {
        TechnologyCard result = technologyCardService.createFromEstimation(estimationDTO);
        return  ResponseEntity.created(new URI("/api/technology-cards/" + result.getId())).body(result);
    }

}
