package eu.canpack.fip.bo.machine;

import eu.canpack.fip.repository.search.MachineSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Machine.
 */
@Service
@Transactional
public class MachineService {

    private final Logger log = LoggerFactory.getLogger(MachineService.class);

    private final MachineRepository machineRepository;

    private final MachineMapper machineMapper;

    private final MachineSearchRepository machineSearchRepository;

    public MachineService(MachineRepository machineRepository, MachineMapper machineMapper, MachineSearchRepository machineSearchRepository) {
        this.machineRepository = machineRepository;
        this.machineMapper = machineMapper;
        this.machineSearchRepository = machineSearchRepository;
    }

    /**
     * Save a machine.
     *
     * @param machineDTO the entity to save
     * @return the persisted entity
     */
    public MachineDTO save(MachineDTO machineDTO) {
        log.debug("Request to save Machine : {}", machineDTO);
        Machine machine = machineMapper.toEntity(machineDTO);
        machine = machineRepository.save(machine);
        MachineDTO result = machineMapper.toDto(machine);
        machineSearchRepository.save(machine);
        return result;
    }

    /**
     *  Get all the machines.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MachineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Machines");
        return machineRepository.findAll(pageable)
            .map(machineMapper::toDto);
    }

    /**
     *  Get one machine by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MachineDTO findOne(Long id) {
        log.debug("Request to get Machine : {}", id);
        Machine machine = machineRepository.findOne(id);
        return machineMapper.toDto(machine);
    }

    /**
     *  Delete the  machine by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Machine : {}", id);
        machineRepository.delete(id);
        machineSearchRepository.delete(id);
    }

    /**
     * Search for the machine corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MachineDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Machines for query {}", query);
        Page<Machine> result = machineSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(machineMapper::toDto);
    }
}
