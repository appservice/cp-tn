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
import javax.xml.ws.Response;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Machine.
 */
@RestController
@RequestMapping("/api")
public class MachineResource {

    private static final String ENTITY_NAME = "machine";
    private final Logger log = LoggerFactory.getLogger(MachineResource.class);
    private final MachineService machineService;
    private final MachineDtlRepository machineDtlRepository;

    public MachineResource(MachineService machineService, MachineDtlRepository machineDtlRepository) {
        this.machineService = machineService;
        this.machineDtlRepository = machineDtlRepository;
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
        MachineDTO result = machineService.create(machineDTO);
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
        MachineDTO result = machineService.update(machineDTO);
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
    public ResponseEntity<List<MachineDTO>> getAllMachines(@ApiParam Pageable pageable, @RequestParam(name = "operationDate", required = false) LocalDate operationDate) {
        log.debug("REST request to get a page of Machines by operationDate: {}", operationDate);
        Page<MachineDTO> page;
        if (operationDate == null) {
            page = machineService.findAll(pageable);
        } else {
            page = machineService.findAll(operationDate, pageable);

        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/machines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /machines/not-pageable : get all the machines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of machines in body
     */
    @GetMapping("/machines/not-pageable")
    @Timed
    public ResponseEntity<List<MachineDTO>> getAllMachinesNotPageable(@ApiParam Pageable pageable, @RequestParam(name = "operationDate", required = false) LocalDate operationDate) {
        log.debug("REST request to get a page of Machines not pageable by operationDate: {}", operationDate);
        List<MachineDTO> list;
        if (operationDate == null) {
            list = machineService.findAllNotPageable(LocalDate.now());
        } else {
            list = machineService.findAllNotPageable(operationDate);

        }

        return new ResponseEntity<>(list,  HttpStatus.OK);
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
     * GET  /machines/:id/with-details : get the "id" machine.
     *
     * @param id the id of the machineDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the machineDTO, or with status 404 (Not Found)
     */
    @GetMapping("/machines/{id}/with-details")
    @Timed
    public ResponseEntity<MachineDTO> getMachineWithDetails(@PathVariable Long id) {
        log.debug("REST request to get Machine : {}", id);
        LocalDate now=LocalDate.now();
        MachineDTO machineDTO = machineService.findOneWithDetailsByOperationDate(id,now);
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


    @GetMapping("machines/{id}/get-machine-dtls")
    @Timed
    public ResponseEntity<List<MachineDtl>> getMachineDtlsByMachineId(@PathVariable Long id) {
        log.debug("REST request tgetMachineDtlsByMachineId  with parameter id: {}", id);

        List<MachineDtl> response = machineDtlRepository.findAllByMachineId(id);
        return ResponseEntity.ok(response);
    }
}
