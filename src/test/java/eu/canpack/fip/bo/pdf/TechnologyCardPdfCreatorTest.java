package eu.canpack.fip.bo.pdf;

import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.machine.Machine;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import eu.canpack.fip.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * CP S.A.
 * Created by lukasz.mochel on 20.08.2017.
 */
public class TechnologyCardPdfCreatorTest {
    public static final String RESULT = "technology2.pdf";
    private static final Logger log = LoggerFactory.getLogger(TechnologyCardPdfCreatorTest.class);
    BarcodeCreatorService barcodeCreatorService = new BarcodeCreatorService();
    private TechnologyCardPdfCreator pdfCreator;

    @Before
    public void setUp() throws Exception {
        pdfCreator = new TechnologyCardPdfCreator(barcodeCreatorService);
    }

    @Test
    public void createPdf() throws Exception {

        OutputStream os = new FileOutputStream(RESULT);

        Estimation est1 = createEstimation();
        pdfCreator.createPdf(est1, os);
        os.close();
    }

    @Test
    public void mergePdfTest() throws Exception {
        PdfUtilService pdfUtilService = new PdfUtilService();

        OutputStream os = new FileOutputStream("JoinedPDF2.pdf");

        Estimation est1=createEstimation();
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        pdfCreator.createPdf(est1, baos1);

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        pdfCreator.createPdf(est1, baos2);

        pdfUtilService.margePdfs(os, baos1, baos2);


        os.close();
        baos1.close();
        baos2.close();
    }

    private Estimation createEstimation() {
        Order order = new Order();
        order.setInternalNumber("12/2107/W");
        Client client = new Client()
            .name("Calsberg Okocim");

        order.setInternalNumber("12/2107/W");
        order.setClient(client);
        order.setReferenceNumber("5ZP2/01/22/W");

        Operation o1 = new Operation()

            .description("Wygrawerować numery\n" +
                             "zgodnie z opisem")
            .estimatedTime(new BigDecimal("0.5"))
            .machine(new Machine().shortcut("Wypal"));
        o1.setId(1021L);

        Operation o2 = new Operation()
            .description("Cięcie")
            .estimatedTime(new BigDecimal("0.2"))
            .machine(new Machine().shortcut("PT"));
        o2.setId(2L);
        Operation o3 = new Operation()
            .description("Hiszpańskie media podają, że w miasteczku Subirats na zachód od Barcelony policja zastrzeliła osobę, która być może jest 22-letnim Junesem Abujakubem\n" +
                             "\n"
            )
            .estimatedTime(new BigDecimal("0.1"))
            .machine(new Machine().shortcut("Sz.P"));
        o3.setId(33L);

        User creator = new User();

        creator.setFirstName("Paweł");
        creator.setLastName("Jedliński");


        Estimation est1 = new Estimation()
            .amount(2)
            .description("Rolka nakładania lakieru fi 73")
            .drawing(new Drawing().number("AB-17-00/A"))
            .finalCost(new BigDecimal("1540.5"))
            .neededRealizationDate(LocalDate.of(2017, 8, 21))
            .createdBy(creator)
            .material("Blacha ST3");
        ;
        est1.setCreatedAt(ZonedDateTime.of(2017, 8, 22, 14, 0, 0, 0, ZoneId.systemDefault()));
        est1.setEstimatedRealizationDate(LocalDate.of(2017, 5, 7));
        est1.setOrder(order);
        est1.getOperations().add(o1);
        est1.getOperations().add(o2);
        est1.getOperations().add(o3);
        est1.getOperations().add(o1);
        est1.getOperations().add(o2);
        est1.getOperations().add(o3);
        est1.getOperations().add(o1);

        est1.getOperations().add(o2);
        est1.getOperations().add(o3);
        est1.getOperations().add(o2);
        est1.getOperations().add(o3);
        est1.getOperations().add(o1);
        est1.getOperations().add(o2);
        est1.getOperations().add(o3);
        log.debug("est1 {}", est1);
        return est1;
    }
}
