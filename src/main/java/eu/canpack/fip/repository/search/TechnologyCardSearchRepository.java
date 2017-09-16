package eu.canpack.fip.repository.search;

import eu.canpack.fip.bo.technologyCard.TechnologyCard;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TechnologyCard entity.
 */
public interface TechnologyCardSearchRepository extends ElasticsearchRepository<TechnologyCard, Long> {
}
