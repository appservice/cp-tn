package eu.canpack.fip.bo.cooperation;


import java.util.List;

import eu.canpack.fip.bo.cooperation.dto.CooperationCriteria;
import eu.canpack.fip.bo.cooperation.dto.CooperationDTO;
import eu.canpack.fip.bo.estimation.Estimation_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import eu.canpack.fip.repository.search.CooperationSearchRepository;

import eu.canpack.fip.bo.cooperation.dto.CooperationMapper;

/**
 * Service for executing complex queries for Cooperation entities in the database.
 * The main input is a {@link CooperationCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link CooperationDTO} or a {@link Page} of {%link CooperationDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class CooperationQueryService extends QueryService<Cooperation> {

    private final Logger log = LoggerFactory.getLogger(CooperationQueryService.class);


    private final CooperationRepository cooperationRepository;

    private final CooperationMapper cooperationMapper;

    private final CooperationSearchRepository cooperationSearchRepository;

    public CooperationQueryService(CooperationRepository cooperationRepository, CooperationMapper cooperationMapper, CooperationSearchRepository cooperationSearchRepository) {
        this.cooperationRepository = cooperationRepository;
        this.cooperationMapper = cooperationMapper;
        this.cooperationSearchRepository = cooperationSearchRepository;
    }

    /**
     * Return a {@link List} of {%link CooperationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CooperationDTO> findByCriteria(CooperationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Cooperation> specification = createSpecification(criteria);
        return cooperationMapper.toDto(cooperationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link CooperationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CooperationDTO> findByCriteria(CooperationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Cooperation> specification = createSpecification(criteria);
        final Page<Cooperation> result = cooperationRepository.findAll(specification, page);
        return result.map(cooperationMapper::toDto);
    }

    /**
     * Function to convert CooperationCriteria to a {@link Specifications}
     */
    private Specifications<Cooperation> createSpecification(CooperationCriteria criteria) {
        Specifications<Cooperation> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), eu.canpack.fip.bo.cooperation.Cooperation_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), eu.canpack.fip.bo.cooperation.Cooperation_.name));
            }
            if (criteria.getCounterparty() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCounterparty(), eu.canpack.fip.bo.cooperation.Cooperation_.counterparty));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), eu.canpack.fip.bo.cooperation.Cooperation_.amount));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), eu.canpack.fip.bo.cooperation.Cooperation_.price));
            }
            if (criteria.getEstimationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEstimationId(), eu.canpack.fip.bo.cooperation.Cooperation_.estimation, Estimation_.id));
            }
        }
        return specification;
    }

}
