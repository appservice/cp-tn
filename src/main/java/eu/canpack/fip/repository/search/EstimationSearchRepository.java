package eu.canpack.fip.repository.search;

import eu.canpack.fip.bo.estimation.Estimation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Estimation entity.
 */
public interface EstimationSearchRepository extends ElasticsearchRepository<Estimation, Long> {
}
