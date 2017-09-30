package eu.canpack.fip.bo.drawing;

import eu.canpack.fip.bo.drawing.dto.DrawingCriteria;
import eu.canpack.fip.bo.drawing.dto.DrawingDTO;

import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for Order entities in the database.
 * The main input is a {@link DrawingCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link OrderDTO} or a {@link Page} of {%link OrderDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class DrawingQueryService extends QueryService<Drawing> {

    private final Logger log = LoggerFactory.getLogger(DrawingQueryService.class);


    private final DrawingRepository drawingRepository;

    private final DrawingMapper drawingMapper;

    public DrawingQueryService(DrawingRepository drawingRepository, DrawingMapper drawingMapper) {
        this.drawingRepository = drawingRepository;
        this.drawingMapper = drawingMapper;
    }

    /**
     * Return a {@link List} of {%link OrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DrawingDTO> findByCriteria(DrawingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Drawing> specification = createSpecification(criteria);
        return drawingMapper.toDto(drawingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link OrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DrawingDTO> findByCriteria(DrawingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Drawing> specification = createSpecification(criteria);
        final Page<Drawing> result = drawingRepository.findAll(specification, page);
        return result.map(drawingMapper::toDto);
    }

    /**
     * Function to convert OrderCriteria to a {@link Specifications}
     */
    private Specifications<Drawing> createSpecification(DrawingCriteria criteria) {
        Specifications<Drawing> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), eu.canpack.fip.bo.drawing.Drawing_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), eu.canpack.fip.bo.drawing.Drawing_.name));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), eu.canpack.fip.bo.drawing.Drawing_.number));
            }

        }
        return specification;
    }

}
