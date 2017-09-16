package eu.canpack.fip.repository.search;

import eu.canpack.fip.bo.drawing.Drawing;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Drawing entity.
 */
public interface DrawingSearchRepository extends ElasticsearchRepository<Drawing, Long> {
}
