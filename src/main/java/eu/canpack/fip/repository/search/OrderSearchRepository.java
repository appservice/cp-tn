package eu.canpack.fip.repository.search;

import eu.canpack.fip.bo.order.Order;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Order entity.
 */
public interface OrderSearchRepository extends ElasticsearchRepository<Order, Long> {
}
