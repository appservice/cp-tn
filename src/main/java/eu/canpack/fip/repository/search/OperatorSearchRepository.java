package eu.canpack.fip.repository.search;

import eu.canpack.fip.domain.Operator;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Operator entity.
 */
public interface OperatorSearchRepository extends ElasticsearchRepository<Operator, Long> {
}
