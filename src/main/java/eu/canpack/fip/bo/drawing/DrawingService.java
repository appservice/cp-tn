package eu.canpack.fip.bo.drawing;

import eu.canpack.fip.repository.search.DrawingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    private final DrawingSearchRepository drawingSearchRepository;

    public DrawingService(DrawingRepository drawingRepository, DrawingMapper drawingMapper, DrawingSearchRepository drawingSearchRepository) {
        this.drawingRepository = drawingRepository;
        this.drawingMapper = drawingMapper;
        this.drawingSearchRepository = drawingSearchRepository;
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
        drawing = drawingRepository.save(drawing);
        DrawingDTO result = drawingMapper.toDto(drawing);
        drawingSearchRepository.save(drawing);
        return result;
    }

    /**
     *  Get all the drawings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DrawingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Drawings");
        return drawingRepository.findAll(pageable)
            .map(drawingMapper::toDto);
    }

    /**
     *  Get one drawing by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public DrawingDTO findOne(Long id) {
        log.debug("Request to get Drawing : {}", id);
        Drawing drawing = drawingRepository.findOne(id);
        return drawingMapper.toDto(drawing);
    }

    /**
     *  Delete the  drawing by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Drawing : {}", id);
        drawingRepository.delete(id);
        drawingSearchRepository.delete(id);
    }

    /**
     * Search for the drawing corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DrawingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Drawings for query {}", query);
        Page<Drawing> result = drawingSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(drawingMapper::toDto);
    }
}
