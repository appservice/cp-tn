package eu.canpack.fip.bo.production;

import eu.canpack.fip.bo.estimation.EstimationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CP S.A.
 * Created by lukasz.mochel on 29.10.2017.
 */
@Service
@Transactional
public class ProductionService {


    private final EstimationRepository estimationRepository;

    public ProductionService(EstimationRepository estimationRepository) {
        this.estimationRepository = estimationRepository;
    }

    public Page<ProductionItemDTO> showActualProduction(Pageable pageable){
        return estimationRepository.getItemsActualInProduction(pageable);
    }
}
