package eu.canpack.fip.bo.drawing;

import eu.canpack.fip.bo.attachment.Attachment;
import eu.canpack.fip.bo.attachment.AttachmentRepository;
import eu.canpack.fip.bo.drawing.dto.DrawingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Drawing.
 */
@Service
@Transactional
public class DrawingService {

    private final Logger log = LoggerFactory.getLogger(DrawingService.class);

    private final DrawingRepository drawingRepository;

    private final DrawingMapper drawingMapper;


    private final AttachmentRepository attachmentRepository;

    public DrawingService(DrawingRepository drawingRepository, DrawingMapper drawingMapper,  AttachmentRepository attachmentRepository) {
        this.drawingRepository = drawingRepository;
        this.drawingMapper = drawingMapper;
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * Save a drawing.
     *
     * @param drawingDTO the entity to save
     * @return the persisted entity
     */
    public DrawingDTO save(DrawingDTO drawingDTO) {
        log.debug("Request to save Drawing : {}", drawingDTO);
        Drawing drawing = drawingMapper.toEntity(drawingDTO);
        drawing.setCreatedAt(ZonedDateTime.now());
        drawing = drawingRepository.save(drawing);
        DrawingDTO result = drawingMapper.toDto(drawing);
        return result;
    }

    /**
     * Save a drawing.
     *
     * @param drawingDTO the entity to save
     * @return the persisted entity
     */
    public DrawingDTO create(DrawingDTO drawingDTO) {
        log.debug("Request to save Drawing : {}", drawingDTO);
        Drawing drawing = drawingMapper.toEntity(drawingDTO);

//        drawing.getAttachments().forEach(a -> a.setDrawing(drawing));
        List<Attachment> attachments = drawing.getAttachments().stream()
            .map(a -> attachmentRepository.findOne(a.getId()))
            .peek(a -> a.setDrawing(drawing)).collect(Collectors.toList());
        drawing.setAttachments(attachments);
        drawing.setCreatedAt(ZonedDateTime.now());
        Drawing response = drawingRepository.save(drawing);
        DrawingDTO result = drawingMapper.toDto(response);
        return result;
    }

    /**
     * Get all the drawings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DrawingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Drawings");
        return drawingRepository.findAll(pageable)
            .map(drawingMapper::toDto);
    }

    /**
     * Get one drawing by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public DrawingDTO findOne(Long id) {
        log.debug("Request to get Drawing : {}", id);
        Drawing drawing = drawingRepository.findOne(id);
        return drawingMapper.toDto(drawing);
    }

    /**
     * Delete the  drawing by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Drawing : {}", id);
        drawingRepository.delete(id);
    }

}
