package eu.canpack.fip.bo.attachment;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
        ZonedDateTime now = ZonedDateTime.now();
        String dateString = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss_SSS"));
        String fileExtensions = FilenameUtils.getExtension(fileName);
        String fileBaseName = FilenameUtils.getBaseName(fileName);
        String pathFileName=fileBaseName+"_"+dateString+"."+fileExtensions;

        Path path = Paths.get(drawingDirectoryPath, pathFileName);

        byte[] bytes = multipartFile.getBytes();
        path = Files.write(path, bytes);

        log.debug("file name: {}", path.toString());
        Attachment attachment = new Attachment();
        attachment.setName(fileName);
        attachment.setPath(path.toString());
        attachment.setDataContentType(multipartFile.getContentType());
        attachment.setUploadDate(now);
//        attachment.setDrawing();
        attachmentRepository.save(attachment);
        return attachment;

    }


    public void deleteAttachment(Long attachmentId) {
        Attachment attachment = attachmentRepository.findOne(attachmentId);
        deleteFile(attachment);
        if (attachment.getDrawing() != null) {
            attachment.getDrawing().getAttachments().remove(attachment);
        }

        attachmentRepository.delete(attachmentId);
    }

    private void deleteFile(Attachment attachment) {
        Path path = Paths.get(attachment.getPath());
        try {
            Files.delete(path);
        } catch (IOException e) {
            log.warn("problem with deleting file {}", path.toString(), e);
        }
    }


    @Scheduled(cron="0 50 0 * * *")
    public void deleteUnusedFile(){
        log.info("Deleting unused file job");
        List<Attachment> attachmentsToDelete=attachmentRepository.findAllByDrawingIsNull()
            .stream()
            .filter(a->a.getUploadDate().plusHours(12).isBefore(ZonedDateTime.now()))
            .peek(a->log.info("Deleting unused fie: {}",a.getPath()))
            .peek(this::deleteFile)
            .collect(Collectors.toList());
      attachmentRepository.delete(attachmentsToDelete);

    }
}
