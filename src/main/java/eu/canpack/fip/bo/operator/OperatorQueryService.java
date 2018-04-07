package eu.canpack.fip.bo.operator;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import eu.canpack.fip.repository.search.OperatorSearchRepository;

import eu.canpack.fip.service.mapper.OperatorMapper;

/**
 * Service for executing complex queries for Operator entities in the database.
 * The main input is a {@link OperatorCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OperatorDTO} or a {@link Page} of {@link OperatorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OperatorQueryService extends QueryService<Operator> {

    private final Logger log = LoggerFactory.getLogger(OperatorQueryService.class);


    private final OperatorRepository operatorRepository;

    private final OperatorMapper operatorMapper;

    private final OperatorSearchRepository operatorSearchRepository;

    public OperatorQueryService(OperatorRepository operatorRepository, OperatorMapper operatorMapper, OperatorSearchRepository operatorSearchRepository) {
        this.operatorRepository = operatorRepository;
        this.operatorMapper = operatorMapper;
        this.operatorSearchRepository = operatorSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OperatorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OperatorDTO> findByCriteria(OperatorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Operator> specification = createSpecification(criteria);
        return operatorMapper.toDto(operatorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OperatorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OperatorDTO> findByCriteria(OperatorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Operator> specification = createSpecification(criteria);
        final Page<Operator> result = operatorRepository.findAll(specification, page);
        return result.map(operatorMapper::toDto);
    }

    /**
     * Function to convert OperatorCriteria to a {@link Specifications}
     */
    private Specifications<Operator> createSpecification(OperatorCriteria criteria) {
        Specifications<Operator> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), eu.canpack.fip.bo.operator.Operator_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), eu.canpack.fip.bo.operator.Operator_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), eu.canpack.fip.bo.operator.Operator_.lastName));
            }
            if (criteria.getCardNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCardNumber(), eu.canpack.fip.bo.operator.Operator_.cardNumber));
            }
            if (criteria.getJobTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobTitle(), eu.canpack.fip.bo.operator.Operator_.jobTitle));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), eu.canpack.fip.bo.operator.Operator_.active));
            }
        }
        return specification;
    }

}
