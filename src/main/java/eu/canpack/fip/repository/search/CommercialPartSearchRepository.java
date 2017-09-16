package eu.canpack.fip.repository.search;

import eu.canpack.fip.bo.commercialPart.CommercialPart;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CommercialPart entity.
 */
public interface CommercialPartSearchRepository extends ElasticsearchRepository<CommercialPart, Long> {
}
