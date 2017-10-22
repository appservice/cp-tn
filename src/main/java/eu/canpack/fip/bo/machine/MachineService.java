package eu.canpack.fip.bo.machine;

import eu.canpack.fip.config.Constants;
import eu.canpack.fip.repository.search.MachineSearchRepository;
import eu.canpack.fip.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.Optional;

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

    private final MachineDtlRepository machineDtlRepository;

    public MachineService(MachineRepository machineRepository, MachineMapper machineMapper, MachineSearchRepository machineSearchRepository, MachineDtlRepository machineDtlRepository) {
        this.machineRepository = machineRepository;
        this.machineMapper = machineMapper;
        this.machineSearchRepository = machineSearchRepository;
        this.machineDtlRepository = machineDtlRepository;
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
     * Save a machine.
     *
     * @param machineDTO the entity to save
     * @return the persisted entity
     */
    public MachineDTO update(MachineDTO machineDTO) {
        log.debug("Request to update Machine : {}", machineDTO);
        Machine machine = machineRepository.findOne(machineDTO.getId());
        machine.setName(machineDTO.getName());
        machine.setShortcut(machineDTO.getShortcut());
       // Machine machine = machineMapper.toEntity(machineDTO);
        Optional<MachineDtl> mDtlOptional = machine.getMachineDtls().stream().filter(m -> m.getValidFrom().equals(machineDTO.getValidFrom())).findFirst();
        if (mDtlOptional.isPresent()) {
            mDtlOptional.get().setWorkingHourPrice(machineDTO.getWorkingHourPrice());
        } else {

            Optional<MachineDtl> machineToChangeOptional = machine.getMachineDtls().stream()
                .filter(mdtl -> mdtl.getValidFrom().isBefore(machineDTO.getValidFrom()) && mdtl.getValidTo().isAfter(machineDTO.getValidFrom()))
                .findFirst();
            if(machineToChangeOptional.isPresent()){
                MachineDtl machineToChange=machineToChangeOptional.get();
                MachineDtl newMachineDtl=new MachineDtl();
                newMachineDtl.setWorkingHourPrice(machineDTO.getWorkingHourPrice());
                newMachineDtl.setValidFrom(machineDTO.getValidFrom());
                newMachineDtl.setValidTo(machineToChange.getValidTo());
                newMachineDtl.setMachine(machine);
                machine.getMachineDtls().add(newMachineDtl);
                machineToChange.setValidTo(machineDTO.getValidFrom().minusDays(1L));
            }else{
                if(machine.getMachineDtls().isEmpty()){
                    MachineDtl machineDtl=new MachineDtl();
                    machineDtl.setValidFrom(Constants.BIG_BANG_DATE);
                    machineDtl.setValidTo(Constants.DATE_UNTIL_NOTICE);
                    machineDtl.setMachine(machine);
                    machineDtl.setWorkingHourPrice(machineDTO.getWorkingHourPrice());
                    machine.getMachineDtls().add(machineDtl);

                }else{
                    throw new CustomParameterizedException("error.machinePeriodsAreDiscontinuous");

                }
            }



        }

        machine = machineRepository.save(machine);
        MachineDTO result = machineMapper.toDto(machine);
        machineSearchRepository.save(machine);
        return result;
    }

    /**
     * Save a machine.
     *
     * @param machineDTO the entity to save
     * @return the persisted entity
     */
    public MachineDTO create(MachineDTO machineDTO) {
        log.debug("Request to create Machine : {}", machineDTO);
        Machine machine = machineMapper.toEntity(machineDTO);
        MachineDtl machineDtl = new MachineDtl();
        machineDtl.setValidFrom(Constants.BIG_BANG_DATE);
        machineDtl.setValidTo(Constants.DATE_UNTIL_NOTICE);
        machineDtl.setWorkingHourPrice(machineDTO.getWorkingHourPrice());
        machineDtl.setMachine(machine);
        machine.getMachineDtls().add(machineDtl);
        machine = machineRepository.save(machine);
        //   MachineDTO result = machineMapper.toDto(machine);
        machineSearchRepository.save(machine);

        return new MachineDTO(machine, machineDtl);
    }

    /**
     * Get all the machines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MachineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Machines");
        return machineRepository.findAllByOperationDate(LocalDate.now(), pageable);
        // .map(machineMapper::toDto);
    }

    /**
     * Get all the machines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MachineDTO> findAll(LocalDate operationDate, Pageable pageable) {
        log.debug("Request to get all Machines");
        return machineRepository.findAllByOperationDate(operationDate, pageable);
    }

    /**
     * Get one machine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MachineDTO findOne(Long id) {
        log.debug("Request to get Machine : {}", id);
        Machine machine = machineRepository.findOne(id);
        return machineMapper.toDto(machine);
    }

    /**
     * Delete the  machine by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Machine : {}", id);
        machineRepository.delete(id);
        machineSearchRepository.delete(id);
    }

    /**
     * Search for the machine corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MachineDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Machines for query {}", query);
        Page<Machine> result = machineSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(machineMapper::toDto);
    }
}
