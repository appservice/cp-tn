package eu.canpack.fip.bo.machine;

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
 * REST controller for managing Machine.
 */
@RestController
@RequestMapping("/api")
public class MachineResource {

    private final Logger log = LoggerFactory.getLogger(MachineResource.class);

    private static final String ENTITY_NAME = "machine";

    private final MachineService machineService;

    public MachineResource(MachineService machineService) {
        this.machineService = machineService;
    }

    /**
     * POST  /machines : Create a new machine.
     *
     * @param machineDTO the machineDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new machineDTO, or with status 400 (Bad Request) if the machine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/machines")
    @Timed
    public ResponseEntity<MachineDTO> createMachine(@Valid @RequestBody MachineDTO machineDTO) throws URISyntaxException {
        log.debug("REST request to save Machine : {}", machineDTO);
        if (machineDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new machine cannot already have an ID")).body(null);
        }
        MachineDTO result = machineService.save(machineDTO);
        return ResponseEntity.created(new URI("/api/machines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /machines : Updates an existing machine.
     *
     * @param machineDTO the machineDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated machineDTO,
     * or with status 400 (Bad Request) if the machineDTO is not valid,
     * or with status 500 (Internal Server Error) if the machineDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/machines")
    @Timed
    public ResponseEntity<MachineDTO> updateMachine(@Valid @RequestBody MachineDTO machineDTO) throws URISyntaxException {
        log.debug("REST request to update Machine : {}", machineDTO);
        if (machineDTO.getId() == null) {
            return createMachine(machineDTO);
        }
        MachineDTO result = machineService.save(machineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, machineDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /machines : get all the machines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of machines in body
     */
    @GetMapping("/machines")
    @Timed
    public ResponseEntity<List<MachineDTO>> getAllMachines(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Machines");
        Page<MachineDTO> page = machineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/machines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /machines/:id : get the "id" machine.
     *
     * @param id the id of the machineDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the machineDTO, or with status 404 (Not Found)
     */
    @GetMapping("/machines/{id}")
    @Timed
    public ResponseEntity<MachineDTO> getMachine(@PathVariable Long id) {
        log.debug("REST request to get Machine : {}", id);
        MachineDTO machineDTO = machineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(machineDTO));
    }

    /**
     * DELETE  /machines/:id : delete the "id" machine.
     *
     * @param id the id of the machineDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/machines/{id}")
    @Timed
    public ResponseEntity<Void> deleteMachine(@PathVariable Long id) {
        log.debug("REST request to delete Machine : {}", id);
        machineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/machines?query=:query : search for the machine corresponding
     * to the query.
     *
     * @param query the query of the machine search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/machines")
    @Timed
    public ResponseEntity<List<MachineDTO>> searchMachines(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Machines for query {}", query);
        Page<MachineDTO> page = machineService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/machines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
