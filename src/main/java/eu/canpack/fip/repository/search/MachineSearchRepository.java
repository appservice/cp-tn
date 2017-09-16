package eu.canpack.fip.repository.search;

import eu.canpack.fip.bo.machine.Machine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Machine entity.
 */
public interface MachineSearchRepository extends ElasticsearchRepository<Machine, Long> {
}
