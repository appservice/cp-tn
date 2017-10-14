package eu.canpack.fip.bo.pdf;

import eu.canpack.fip.bo.operator.OperatorDTO;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * CP S.A.
 * Created by lukasz.mochel on 10.10.2017.
 */
public class OperatorCardCreatorServiceTest {

   private  BarcodeCreatorService barcodeCreatorService = new BarcodeCreatorService();
   private OperatorCardCreatorService operatorCardCreatorService;

    @Before
    public void setUp() throws Exception {
        operatorCardCreatorService=new OperatorCardCreatorService(barcodeCreatorService);
    }

    @Test
    public void createOperatorCard() throws Exception {

        OutputStream os = new FileOutputStream("operatorCard.pdf");
        OperatorDTO operator=prepareOperatorStub();
        operatorCardCreatorService.createOperatorCard(os,operator);
        os.close();

    }

    private OperatorDTO prepareOperatorStub(){
        OperatorDTO operator=new OperatorDTO();
           operator.setActive(true);
        operator.setFirstName("Jan");
        operator.setLastName("Kowalski");
        operator.setCardNumber("01007722");
        operator.setJobTitle("Frezer");

        return operator;

    }
}
