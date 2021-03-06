package eu.canpack.fip.bo.technologyCard;

import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.drawing.Drawing_;
import eu.canpack.fip.bo.technologyCard.mapper.TechnologyCardListDTO;
import eu.canpack.fip.bo.technologyCard.mapper.TechnologyCardListMapper;
import eu.canpack.fip.domain.User_;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for TechnologyCard entities in the database.
 * The main input is a {@link TechnologyCardCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link TechnologyCardListDTO} or a {@link Page} of {%link TechnologyCardListDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class TechnologyCardQueryService extends QueryService<TechnologyCard> {

    private final Logger log = LoggerFactory.getLogger(TechnologyCardQueryService.class);


    private final TechnologyCardRepository TechnologyCardRepository;

    private final TechnologyCardListMapper TechnologyCardListMapper;

    public TechnologyCardQueryService(TechnologyCardRepository TechnologyCardRepository, eu.canpack.fip.bo.technologyCard.mapper.TechnologyCardListMapper TechnologyCardListMapper) {
        this.TechnologyCardRepository = TechnologyCardRepository;
        this.TechnologyCardListMapper = TechnologyCardListMapper;
    }

    /**
     * Return a {@link List} of {%link TechnologyCardListDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TechnologyCardListDTO> findByCriteria(TechnologyCardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TechnologyCard> specification = createSpecification(criteria);
        return TechnologyCardListMapper.toDto(TechnologyCardRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link TechnologyCardListDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TechnologyCardListDTO> findByCriteria(TechnologyCardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TechnologyCard> specification = createSpecification(criteria);
        final Page<TechnologyCard> result = TechnologyCardRepository.findAll(specification, page);
        return result.map(TechnologyCardListMapper::toDto);
    }

    /**
     * Function to convert TechnologyCardCriteria to a {@link Specifications}
     */
    private Specifications<TechnologyCard> createSpecification(TechnologyCardCriteria criteria) {
        Specifications<TechnologyCard> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), eu.canpack.fip.bo.technologyCard.TechnologyCard_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), eu.canpack.fip.bo.technologyCard.TechnologyCard_.description));
            }

            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), eu.canpack.fip.bo.technologyCard.TechnologyCard_.createdAt));
            }

            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreatedById(), eu.canpack.fip.bo.technologyCard.TechnologyCard_.createdBy, User_.id));
            }


            if (criteria.getCreatedByLastName() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreatedByLastName(), eu.canpack.fip.bo.technologyCard.TechnologyCard_.createdBy, User_.lastName));
            }
            if (criteria.getCreatedByFirstName() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreatedByFirstName(), eu.canpack.fip.bo.technologyCard.TechnologyCard_.createdBy, User_.firstName));
            }
            if (criteria.getDrawingNumber()!=null) {
                log.debug("drawingNumber {}",criteria.getDrawingNumber().getContains());
                Specification<TechnologyCard> spec = (root, query, builder) -> {
                    Join<TechnologyCard,Drawing> joinToDrawing=root.join(eu.canpack.fip.bo.technologyCard.TechnologyCard_.drawing,JoinType.INNER);
                   return builder.like(builder.upper(joinToDrawing.get(Drawing_.number)), "%"+criteria.getDrawingNumber().getContains().trim().toUpperCase()+"%" );
                };
                specification=specification.and(spec);

            }
            if (criteria.getDrawingName()!=null) {
                log.debug("drawinName {}",criteria.getDrawingName().getContains());
                Specification<TechnologyCard> spec = (root, query, builder) -> {
                    Join<TechnologyCard,Drawing> joinToDrawing=root.join(eu.canpack.fip.bo.technologyCard.TechnologyCard_.drawing,JoinType.INNER);
                   return builder.like(builder.upper(joinToDrawing.get(Drawing_.name)), "%"+criteria.getDrawingName().getContains().trim().toUpperCase()+"%" );
                };
                specification=specification.and(spec);

            }
        }
        return specification;
    }


}
