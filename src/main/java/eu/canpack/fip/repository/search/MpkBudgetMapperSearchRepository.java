package eu.canpack.fip.repository.search;

import eu.canpack.fip.domain.MpkBudgetMapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MpkBudgetMapper entity.
 */
public interface MpkBudgetMapperSearchRepository extends ElasticsearchRepository<MpkBudgetMapper, Long> {
}
