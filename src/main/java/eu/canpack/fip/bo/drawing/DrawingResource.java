package eu.canpack.fip.bo.drawing;

import com.codahale.metrics.annotation.Timed;
import eu.canpack.fip.bo.drawing.dto.DrawingCriteria;
import eu.canpack.fip.bo.drawing.dto.DrawingDTO;
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
 * REST controller for managing Drawing.
 */
@RestController
@RequestMapping("/api")
public class DrawingResource {

    private final Logger log = LoggerFactory.getLogger(DrawingResource.class);

    private static final String ENTITY_NAME = "drawing";

    private final DrawingService drawingService;

    private final DrawingQueryService drawingQueryService;

    public DrawingResource(DrawingService drawingService, DrawingQueryService drawingQueryService) {
        this.drawingService = drawingService;
        this.drawingQueryService = drawingQueryService;
    }

    /**
     * POST  /drawings : Create a new drawing.
     *
     * @param drawingDTO the drawingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new drawingDTO, or with status 400 (Bad Request) if the drawing has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/drawings")
    @Timed
    public ResponseEntity<DrawingDTO> createDrawing(@Valid @RequestBody DrawingDTO drawingDTO) throws URISyntaxException {
        log.debug("REST request to save Drawing : {}", drawingDTO);
        if (drawingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new drawing cannot already have an ID")).body(null);
        }
        DrawingDTO result = drawingService.create(drawingDTO);
        return ResponseEntity.created(new URI("/api/drawings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /drawings : Updates an existing drawing.
     *
     * @param drawingDTO the drawingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated drawingDTO,
     * or with status 400 (Bad Request) if the drawingDTO is not valid,
     * or with status 500 (Internal Server Error) if the drawingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/drawings")
    @Timed
    public ResponseEntity<DrawingDTO> updateDrawing(@Valid @RequestBody DrawingDTO drawingDTO) throws URISyntaxException {
        log.debug("REST request to update Drawing : {}", drawingDTO);
        if (drawingDTO.getId() == null) {
            return createDrawing(drawingDTO);
        }
        DrawingDTO result = drawingService.save(drawingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, drawingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /drawings : get all the drawings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of drawings in body
     */
    @GetMapping("/drawings")
    @Timed
    public ResponseEntity<List<DrawingDTO>> getAllDrawings(DrawingCriteria drawingCriteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Drawings");
        Page<DrawingDTO> page = drawingQueryService.findByCriteria(drawingCriteria,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/drawings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /drawings/:id : get the "id" drawing.
     *
     * @param id the id of the drawingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the drawingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/drawings/{id}")
    @Timed
    public ResponseEntity<DrawingDTO> getDrawing(@PathVariable Long id) {
        log.debug("REST request to get Drawing : {}", id);
        DrawingDTO drawingDTO = drawingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(drawingDTO));
    }

    /**
     * DELETE  /drawings/:id : delete the "id" drawing.
     *
     * @param id the id of the drawingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/drawings/{id}")
    @Timed
    public ResponseEntity<Void> deleteDrawing(@PathVariable Long id) {
        log.debug("REST request to delete Drawing : {}", id);
        drawingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


}
