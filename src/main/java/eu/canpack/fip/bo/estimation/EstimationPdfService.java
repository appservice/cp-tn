package eu.canpack.fip.bo.estimation;

import eu.canpack.fip.bo.pdf.TechnologyCardPdfCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;

/**
 * CP S.A.
 * Created by lukasz.mochel on 24.08.2017.
 */
@Service
@Transactional(readOnly = true)
public class EstimationPdfService {

    private final TechnologyCardPdfCreator technologyCardPdfCreator;
    private final EstimationRepository estimationRepository;

    public EstimationPdfService(TechnologyCardPdfCreator technologyCardPdfCreator, EstimationRepository estimationRepository) {
        this.technologyCardPdfCreator = technologyCardPdfCreator;
        this.estimationRepository = estimationRepository;
    }

    public void createTechnology(Long estimationId, OutputStream outputStream){
        Estimation estimation=estimationRepository.findOne(estimationId);
        if(estimation!=null){
            technologyCardPdfCreator.createPdf(estimation,outputStream);

        }
    }
}
