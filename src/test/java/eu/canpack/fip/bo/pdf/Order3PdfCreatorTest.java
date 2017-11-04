package eu.canpack.fip.bo.pdf;

import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.estimation.dto.EstimationCreateDTO;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.OrderRepository;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import eu.canpack.fip.domain.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


/**
 * CP S.A.
 * Created by lukasz.mochel on 20.07.2017.
 */
public class Order3PdfCreatorTest {
    public static final String RESULT = "order3.pdf";
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    private Order3PdfCreator pdfCreator;
    @Mock
    private OrderRepository orderRepository;

    private PdfUtilService pdfUtilService;

    @Before
    public void setUp() throws Exception {

        when(orderRepository.findOne(Mockito.any(Long.class))).thenReturn(getStubOrder());
        pdfCreator = new Order3PdfCreator(orderRepository);

    }

    @Test
    public void createPdf() throws Exception {

        OutputStream os = new FileOutputStream(RESULT);
        OrderDTO orderDTO = getStubOrderDTO();

        pdfCreator.createPdf(orderDTO, os);
        os.close();
    }

    Order getStubOrder() {
        Order order = new Order();
        order.setInternalNumber("12/2107/W");
        Client client = new Client()
            .name("Calsberg Okocim");

        order.setInternalNumber("12/2107/W");
        order.setClient(client);

        User estimationMaker=new User();
        estimationMaker.setFirstName("Paweł");
        estimationMaker.setLastName("Jedliński");
        estimationMaker.setEmail("pawel.jedlinski@canpack.eu");

        order.setEstimationMaker(estimationMaker);
        Estimation est1 = new Estimation()
            .amount(2)
            .description("Rolka nakładania lakieru fi 73")
            .itemName("Rolka nakładania lakieru fi 73")
            .drawing(new Drawing().number("AB-17-00/A"))
            .itemNumber("AB-17-00/A")
            .finalCost(new BigDecimal("1540.5"))
            .estimatedCost(new BigDecimal("1540.5"))
            .neededRealizationDate(LocalDate.of(2017, 8, 21));
        est1.setId(1L);
        est1.setEstimatedRealizationDate(LocalDate.of(2017, 9, 9));
        order.getEstimations().add(est1);
        User creator=new User();
        creator.setFirstName("Jan");
        creator.setLastName("Dobranowski");
        order.setReferenceNumber("5ZP-3/ZAP/1/2017");


        order.setCreatedBy(creator);


        Estimation est2 = new Estimation()
            .amount(10)
            .description("Rolka nakładania lakieru ")
            .drawing(new Drawing().number("AB-17-00/A"))
            .finalCost(new BigDecimal("520.5"))
            .estimatedCost(new BigDecimal("520.5"))
            .neededRealizationDate(LocalDate.of(2017, 8, 21));
        est2.setEstimatedRealizationDate(LocalDate.of(2017, 12, 9));

        est2.setId(2L);
        order.getEstimations().add(est2);

        Estimation est3 = new Estimation()
            .amount(10)
            .description("Rolka nakładania lakieru fi 84")
            .drawing(new Drawing().number("AB-17-00/A"))
            .estimatedCost(new BigDecimal("520.5"))
            .neededRealizationDate(LocalDate.of(2017, 8, 21));
        est3.setEstimatedRealizationDate(LocalDate.of(2017, 12, 2));

        est3.setId(3L);
        order.getEstimations().add(est3);

        return order;
    }

    OrderDTO getStubOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(2L);
        List<EstimationCreateDTO> estList = new ArrayList<>();

        EstimationCreateDTO e1 = new EstimationCreateDTO();
        e1.setChecked(true);
        e1.setId(1L);
        estList.add(e1);


        EstimationCreateDTO e2 = new EstimationCreateDTO();
        e2.setChecked(true);
        e2.setId(2L);
        estList.add(e2);

        EstimationCreateDTO e3 = new EstimationCreateDTO();
        e3.setChecked(false);
        e3.setId(3L);
        estList.add(e3);

        orderDTO.setEstimations(estList);

        return orderDTO;
    }

    @Test
    public void testOrderJoiner() throws Exception {
        pdfUtilService = new PdfUtilService();
        OutputStream os = new FileOutputStream("JoinedPDF.pdf");
        OrderDTO orderDTO = getStubOrderDTO();

        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        pdfCreator.createPdf(orderDTO, baos1);

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        pdfCreator.createPdf(orderDTO, baos2);

        pdfUtilService.margePdfs(os, baos1, baos2);



        os.close();
        baos1.close();
        baos2.close();
    }
}
