package eu.canpack.fip.bo.attachment;



import com.codahale.metrics.annotation.Timed;
import eu.canpack.fip.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * REST controller for managing FileUploadResource.
 */
@RestController
@RequestMapping("/api")
public class AttachmentResource {
    private static final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    private final AttachmentService attachmentService;
    private final AttachmentRepository attachmentRepository;

    public AttachmentResource(AttachmentService attachmentService, AttachmentRepository attachmentRepository) {
        this.attachmentService = attachmentService;
        this.attachmentRepository = attachmentRepository;
    }

    @PostMapping("/upload")
    @Timed
    public Attachment upload(@RequestParam("file") MultipartFile multipartFile/*,, @RequestParam("filename") String fileName*//*@RequestHeader("fileName")String fileName*/) throws IOException {
       // String decodedToUTF8 = new String(multipartFile.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
        log.debug("uploaded file name: {}, size: {}",multipartFile.getOriginalFilename(),multipartFile.getSize()/1024);

      return  attachmentService.upload(multipartFile,/*fileName*/multipartFile.getOriginalFilename());
    }

    @DeleteMapping("/attachments/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttachment(@PathVariable("id") Long attachmentId){

            attachmentService.deleteAttachment(attachmentId);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/attachments/download/{id}",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Timed
    public ResponseEntity<Resource> downloadFile(@PathVariable("id")Long attachmentId){
        Attachment attachment = attachmentRepository.findOne(attachmentId);
        try {
        if(attachment!=null){
            Path path = Paths.get(attachment.getPath());
            InputStreamResource inputStreamResource = new InputStreamResource(Files.newInputStream(path));


            return    ResponseEntity.ok()
                .contentLength(path.toFile().length())
                .contentType(MediaType.parseMediaType(attachment.getDataContentType()))
                .header("Content-disposition", "attachment;filename=" + path.getFileName())
                .body(inputStreamResource);

        }

        } catch (IOException e) {
            log.error("download file exception",e);
            throw new CustomParameterizedException("Błąd przy ściąganiu pliku ", e.getClass().getName()+"| "+ e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }
}
