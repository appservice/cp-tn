package eu.canpack.fip.repository.search;

import eu.canpack.fip.bo.cooperation.Cooperation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cooperation entity.
 */
public interface CooperationSearchRepository extends ElasticsearchRepository<Cooperation, Long> {
}
