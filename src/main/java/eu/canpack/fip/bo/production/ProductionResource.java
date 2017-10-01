package eu.canpack.fip.bo.production;

/**
 * CP S.A.
 * Created by lukasz.mochel on 01.10.2017.
 */

import com.lowagie.text.DocumentException;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.estimation.EstimationCreateDTO;
import eu.canpack.fip.bo.estimation.EstimationRepository;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import eu.canpack.fip.bo.pdf.PdfUtilService;
import eu.canpack.fip.bo.pdf.TechnologyCardPdfCreator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing Order.
 */
@RestController
@RequestMapping("/api")
public class ProductionResource {

    private final EstimationRepository estimationRepository;
    private final PdfUtilService pdfUtilService;
    private final TechnologyCardPdfCreator technologyCardPdfCreator;

    public ProductionResource(EstimationRepository estimationRepository, PdfUtilService pdfUtilService, TechnologyCardPdfCreator technologyCardPdfCreator) {
        this.estimationRepository = estimationRepository;
        this.pdfUtilService = pdfUtilService;
        this.technologyCardPdfCreator = technologyCardPdfCreator;
    }


    @PostMapping("/production/create-technology-card-pdf")
    @Transactional
    public ResponseEntity<InputStreamResource> getTechnologyCard(@RequestBody OrderDTO orderDTO) throws IOException, DocumentException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        List<Estimation> estimations=orderDTO.getEstimations().stream()
            .filter(EstimationCreateDTO::isChecked)
            .map(e -> estimationRepository.findOne(e.getId())).collect(Collectors.toList());

        ByteArrayOutputStream[] outputStreams = new ByteArrayOutputStream[estimations.size()];

        int i=0;
        for(Estimation estimation:estimations){
            try(ByteArrayOutputStream baos=new ByteArrayOutputStream()){
                technologyCardPdfCreator.createPdf(estimation, baos);
                outputStreams[i]=baos;
                i++;
            }

        }
        pdfUtilService.margePdfs(os,outputStreams);


        InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        os.close();
        inputStream.close();



        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "oferta.pdf")
            .contentType(MediaType.APPLICATION_PDF).body(inputStreamResource);

    }
}
