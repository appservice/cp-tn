package eu.canpack.fip.bo.operation;

import com.codahale.metrics.annotation.Timed;
import eu.canpack.fip.bo.operation.dto.OperationDTO;
import eu.canpack.fip.bo.operation.dto.OperationEventDTO;
import eu.canpack.fip.bo.operation.dto.OperationReportDTO;
import eu.canpack.fip.bo.operation.dto.OperationWideDTO;
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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Operation.
 */
@RestController
@RequestMapping("/api")
public class OperationResource {

    private final Logger log = LoggerFactory.getLogger(OperationResource.class);

    private static final String ENTITY_NAME = "operation";

    private final OperationService operationService;

    public OperationResource(OperationService operationService) {
        this.operationService = operationService;
    }

    /**
     * POST  /operations : Create a new operation.
     *
     * @param operationDTO the operationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new operationDTO, or with status 400 (Bad Request) if the operation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/operations")
    @Timed
    public ResponseEntity<OperationDTO> createOperation(@Valid @RequestBody OperationDTO operationDTO) throws URISyntaxException {
        log.debug("REST request to save Operation : {}", operationDTO);
        if (operationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new operation cannot already have an ID")).body(null);
        }
        OperationDTO result = operationService.save(operationDTO);
        return ResponseEntity.created(new URI("/api/operations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /operations : Updates an existing operation.
     *
     * @param operationDTO the operationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated operationDTO,
     * or with status 400 (Bad Request) if the operationDTO is not valid,
     * or with status 500 (Internal Server Error) if the operationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/operations")
    @Timed
    public ResponseEntity<OperationDTO> updateOperation(@Valid @RequestBody OperationDTO operationDTO) throws URISyntaxException {
        log.debug("REST request to update Operation : {}", operationDTO);
        if (operationDTO.getId() == null) {
            return createOperation(operationDTO);
        }
        OperationDTO result = operationService.save(operationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, operationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /operations : get all the operations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/operations")
    @Timed
    public ResponseEntity<List<OperationDTO>> getAllOperations(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Operations");
        Page<OperationDTO> page = operationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/operations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /operations/:id : get the "id" operation.
     *
     * @param id the id of the operationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the operationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/operations/{id}")
    @Timed
    public ResponseEntity<OperationDTO> getOperation(@PathVariable Long id) {
        log.debug("REST request to get Operation : {}", id);
        OperationDTO operationDTO = operationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(operationDTO));
    }

    /**
     * GET  /operations/:id : get the "id" operation.
     *
     * @param id the id of the operationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the operationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/operations/{id}/wide")
    @Timed
    public ResponseEntity<OperationWideDTO> getOperationWide(@PathVariable Long id) {
        log.debug("REST request to get Operation wide : {}", id);
        OperationWideDTO operationDTO = operationService.findOneWide(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(operationDTO));
    }

    /**
     * DELETE  /operations/:id : delete the "id" operation.
     *
     * @param id the id of the operationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/operations/{id}")
    @Timed
    public ResponseEntity<Void> deleteOperation(@PathVariable Long id) {
        log.debug("REST request to delete Operation : {}", id);
        operationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/operations/{estimationId}/operationsReport")
    public ResponseEntity<List<OperationReportDTO>> getOperationsReport(@PathVariable Long estimationId){
        log.debug("REST request to search for a list of Operations report for estimationId {}", estimationId);
        List<OperationReportDTO> result = operationService.getOperationReports(estimationId);
        return ResponseEntity.ok(result);

    }


    @PutMapping("/operations/updateOperationsStatus")
    public ResponseEntity<Void> updateOperationsStatus(@RequestBody  List<OperationReportDTO> operationReportList){
        log.debug("REST request to search for update Operations report list {}", operationReportList);
        operationService.updateOperationsStatus(operationReportList);
       // List<OperationReportDTO> result = operationService.getOperationReports(estimationId);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/operations/{estimationId}/set-all-operations-finished")
    public ResponseEntity<Void> setAllOperationsFinished(@PathVariable Long estimationId){
        log.debug("REST request setAllOperationsFinished for estimationId {}", estimationId);
        operationService.setAllOperationsFinished(estimationId);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/operations/add-operation-event")
    public ResponseEntity<Long> addOperationEvent(@RequestBody OperationEventDTO operationEventDTO){
        log.debug("REST request for add operation event  {}", operationEventDTO);
        operationEventDTO.setCreatedAt(ZonedDateTime.now());
       Long response= operationService.addOperationEvent(operationEventDTO);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/operations/operation-event/{operationEventId}")
    public ResponseEntity<Void> deleteOperationEvent(@PathVariable("operationEventId") Long operationEventId){
        log.debug("REST request to delete operation event  {}", operationEventId);
        operationService.deleteOperationEvent(operationEventId);

        return ResponseEntity.noContent().build();

    }
}
