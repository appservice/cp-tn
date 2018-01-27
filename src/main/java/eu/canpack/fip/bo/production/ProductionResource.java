package eu.canpack.fip.bo.production;

/**
 * CP S.A.
 * Created by lukasz.mochel on 01.10.2017.
 */

import com.lowagie.text.DocumentException;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.estimation.dto.EstimationCreateDTO;
import eu.canpack.fip.bo.estimation.EstimationRepository;
import eu.canpack.fip.bo.estimation.dto.EstimationCriteria;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.bo.pdf.OrderSummary2PdfCreator;
import eu.canpack.fip.bo.pdf.PdfUtilService;
import eu.canpack.fip.bo.pdf.TechnologyCardPdfCreator;
import eu.canpack.fip.web.rest.util.PaginationUtil;
import io.github.jhipster.service.filter.BooleanFilter;
import io.swagger.annotations.ApiParam;
import org.hibernate.criterion.Order;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
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
    private final OrderSummary2PdfCreator orderSummaryPdfCreator;
    private final ProductionService productionService;

    public ProductionResource(EstimationRepository estimationRepository, PdfUtilService pdfUtilService, TechnologyCardPdfCreator technologyCardPdfCreator, OrderSummary2PdfCreator orderSummaryPdfCreator, ProductionService productionService) {
        this.estimationRepository = estimationRepository;
        this.pdfUtilService = pdfUtilService;
        this.technologyCardPdfCreator = technologyCardPdfCreator;

        this.orderSummaryPdfCreator = orderSummaryPdfCreator;
        this.productionService = productionService;
    }


    @PostMapping("/production/create-technology-card-pdf")
    @Transactional(readOnly = true)
    public ResponseEntity<InputStreamResource> getTechnologyCard(@RequestBody OrderDTO orderDTO) throws IOException, DocumentException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();


        List<Estimation> estimations = orderDTO.getEstimations().stream()
            .filter(EstimationCreateDTO::isChecked)
            .map(e -> estimationRepository.findOne(e.getId())).collect(Collectors.toList());

        ByteArrayOutputStream[] outputStreams;
        int estNumber=0;
        Boolean printSinglePdfSummaryPerOrderItem=estimations.get(0).getOrder().getClient().getPrintSinglePdfSummaryPerOrderItem();
        if (printSinglePdfSummaryPerOrderItem!=null && printSinglePdfSummaryPerOrderItem) {

            outputStreams = new ByteArrayOutputStream[estimations.size() * 2];

            for(Estimation es:estimations){
                ByteArrayOutputStream baosMainPage = new ByteArrayOutputStream();

                List<Estimation> est = new ArrayList<>();
                est.add(es);
                orderSummaryPdfCreator.createPdf(estimations.get(0).getOrder(), est, baosMainPage);
                outputStreams[estNumber] = baosMainPage;
                estNumber++;
            }

        } else {
            ByteArrayOutputStream baosMainPage = new ByteArrayOutputStream();

            outputStreams = new ByteArrayOutputStream[estimations.size() + 1];
            orderSummaryPdfCreator.createPdf(estimations.get(0).getOrder(), estimations, baosMainPage);
            outputStreams[0] = baosMainPage;
            estNumber++;
        }


        //int i = 1;
        for (Estimation estimation : estimations) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                technologyCardPdfCreator.createPdf(estimation, baos);
                outputStreams[estNumber] = baos;
                estNumber++;
            }

        }
        pdfUtilService.margePdfs(os, outputStreams);


        InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        os.close();
        inputStream.close();
//        for(OutputStream outputStream: outputStreams){
//            outputStream.close();
//        }


        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "oferta.pdf")
            .contentType(MediaType.APPLICATION_PDF).body(inputStreamResource);

    }

    @GetMapping("/production/to-excel")
    @Transactional(readOnly = true)
    public byte[] getTechnologyCard(EstimationCriteria criteria)  {
        Sort sort = new Sort(Sort.Direction.DESC,"productionStartDateTime");
        if(criteria.getOrderStatusFilter()==null){
            criteria.setOrderStatusFilter(new EstimationCriteria.OrderStatusFilter());
        }
        criteria.getOrderStatusFilter().setEquals(OrderStatus.IN_PRODUCTION);

        if(criteria.isFinished()==null){
            criteria.setFinished(new BooleanFilter());
        }
        criteria.isFinished().setEquals(false);

        return productionService.makeExcelOfActualInProduction(criteria, sort);

    }

    @GetMapping("/production/finished/to-excel")
    @Transactional(readOnly = true)
    public byte[] getFinishedProductionItemsToExcel(EstimationCriteria criteria)  {
        Sort sort = new Sort(Sort.Direction.DESC,"deliveredAt");

        if(criteria.isFinished()==null){
            criteria.setFinished(new BooleanFilter());
        }
        criteria.isFinished().setEquals(true);

        return productionService.makeExcelOfFinishedProduction(criteria, sort);

    }

    @GetMapping("production/items-finished")
    public ResponseEntity<List<ProductionItemDeliveredDTO>> showItemsFinished(EstimationCriteria criteria, @ApiParam Pageable pageable) {


        if(criteria.isFinished()==null){
            criteria.setFinished(new BooleanFilter());
        }
        criteria.isFinished().setEquals(true);

        Page<ProductionItemDeliveredDTO> page = productionService.showArchivalProductionByCriteriaAndClient(criteria, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/production/items-finished");

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("production/items-actual-in-production")
    public ResponseEntity<List<ProductionItemDTO>> showItemsActualInProduction(EstimationCriteria criteria, @ApiParam Pageable pageable) {
//        Page<ProductionItemDTO> page = productionService.showActualProduction(pageable);
        if(criteria.getOrderStatusFilter()==null){
            criteria.setOrderStatusFilter(new EstimationCriteria.OrderStatusFilter());
        }
        criteria.getOrderStatusFilter().setEquals(OrderStatus.IN_PRODUCTION);

        if(criteria.isFinished()==null){
            criteria.setFinished(new BooleanFilter());
        }
        criteria.isFinished().setEquals(false);

        Page<ProductionItemDTO> page = productionService.showActualInProductionByCriteriaAndClient(criteria, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/production/items-actual-in-production");

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("production/{productionId}/moveToArchive")
    public ResponseEntity<Void> moveProductionItemToArchive(@PathVariable Long productionId,@RequestParam(required = false) String receiver){
        productionService.moveProductionItemToArchive(productionId,receiver);
        return ResponseEntity.ok().build();
    }

    @GetMapping("production/return-to-technology-verification/{orderId}")
    public ResponseEntity<Void> returnToTechnologyVerification(@PathVariable Long orderId){
        productionService.returnToTechnologyVerification( orderId);
        return ResponseEntity.ok().build();
    }

}
