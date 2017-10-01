package eu.canpack.fip.bo.pdf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * CP S.A.
 * Created by lukasz.mochel on 20.08.2017.
 */
public class BarcodeCreatorServiceTest {

    private BarcodeCreatorService barcodeCreatorService;

    private OutputStream os;
    private static final String FILE_NAME="barcode.png";

    @Before
    public void setUp() throws Exception {
        barcodeCreatorService=new BarcodeCreatorService();
        os = new BufferedOutputStream(new FileOutputStream(FILE_NAME));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createBarcode() throws Exception {
        System.out.println("ówdzie świeci bije dzieci żędną");

        barcodeCreatorService.createBarcode("skoczek",os);
    }

}
