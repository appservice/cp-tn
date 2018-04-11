package eu.canpack.fip.bo.fxClient;

import com.codahale.metrics.annotation.Timed;
import eu.canpack.fip.bo.operation.OperationService;
import eu.canpack.fip.bo.operation.dto.OperationEventDTO;
import eu.canpack.fip.bo.operation.dto.OperationWideDTO;
import eu.canpack.fip.security.AuthoritiesConstants;
import eu.canpack.fip.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * CP S.A.
 * Created by lukasz.mochel on 07.04.2018.
 */
@RestController
@RequestMapping("/api/fx-client")
public class FxClientResource {
    private static final String ENTITY_NAME = "operation";


    private static final Logger log = LoggerFactory.getLogger(FxClientResource.class);

    private final OperationService operationService;
    private String filePath;

    public FxClientResource(
        @Value("${fx-client.filePath}") String filePath,
        OperationService operationService) {
        this.operationService = operationService;
        this.filePath = filePath;

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

    @PutMapping("/operations/add-operation-event")
    public ResponseEntity<Void> addOperationEvent(@RequestBody OperationEventDTO operationEventDTO) {
        log.debug("REST request for add operation event  {}", operationEventDTO);
        operationEventDTO.setCreatedAt(ZonedDateTime.now());
        operationService.addOperationEvent(operationEventDTO);

        return ResponseEntity.ok().build();

    }


    @Secured(AuthoritiesConstants.ADMIN)
    @GetMapping(value = "/client-resource/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void getFile(HttpServletResponse response) throws IOException {
        log.debug("REST request for file from path  {}", filePath);

        byte[] reportBytes = null;
        File dir = new File(filePath);
        log.debug("absolute path {}",dir.getAbsolutePath());
        File result=null;
        if(dir.isDirectory() && dir.listFiles()!=null && Objects.requireNonNull(dir.listFiles()).length>0 ){
            result = Objects.requireNonNull(dir.listFiles())[0];
        }

        if (result!=null && result.exists()) {
            InputStream inputStream = new FileInputStream(result);
            String type = result.toURL().openConnection().guessContentTypeFromName(result.getName());
            response.setHeader("Content-Disposition", "attachment; filename=" + result.getName());
            response.setHeader("Content-Type", type);

            reportBytes = new byte[100];//New change
            OutputStream os = response.getOutputStream();//New change
            int read;
            while ((read = inputStream.read(reportBytes)) != -1) {
                os.write(reportBytes, 0, read);
            }
            os.flush();
            os.close();
        }

    }

}
