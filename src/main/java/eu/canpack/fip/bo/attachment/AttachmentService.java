package eu.canpack.fip.bo.attachment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;

/**
 * CP S.A.
 * Created by lukasz.mochel on 27.06.2017.
 */
@Service
@Transactional
public class AttachmentService {
    private static final Logger log = LoggerFactory.getLogger(AttachmentService.class);
    private final AttachmentRepository attachmentRepository;
    @Value("${application.drawingDirectoryPath}")
    private String drawingDirectoryPath;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }


    public Attachment upload(MultipartFile multipartFile, String fileName) throws IOException {
        Path path = Paths.get(drawingDirectoryPath, fileName);

        byte[] bytes = multipartFile.getBytes();
        path = Files.write(path, bytes);

        log.debug("file name: {}", path.toString());
        Attachment attachment = new Attachment();
        attachment.setName(fileName);
        attachment.setPath(path.toString());
        attachment.setDataContentType(multipartFile.getContentType());
        attachment.setUploadDate(ZonedDateTime.now());
//        attachment.setDrawing();
        attachmentRepository.save(attachment);
        return attachment;

    }



    public void deleteAttachment(Long attachmentId) {
        Attachment attachment = attachmentRepository.findOne(attachmentId);
        Path path = Paths.get(attachment.getPath());
        try {
            Files.delete(path);
        } catch (IOException e) {
            log.warn("problem with deleting file {}", path.toString(),e);
        }
        if (attachment.getDrawing() != null) {
            attachment.getDrawing().getAttachments().remove(attachment);
        }

        attachmentRepository.delete(attachmentId);
    }



}
