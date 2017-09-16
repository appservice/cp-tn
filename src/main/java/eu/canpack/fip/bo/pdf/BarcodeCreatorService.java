package eu.canpack.fip.bo.pdf;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * CP S.A.
 * Created by lukasz.mochel on 19.07.2017.
 */
@Service
public class BarcodeCreatorService {
    private static final Logger log = LoggerFactory.getLogger(BarcodeCreatorService.class);

    public void createBarcode(String text, OutputStream os) {

        BitMatrix bitMatrix;
        Writer writer = new Code128Writer();
        try {
            HashMap hintMap = new HashMap();
            hintMap.put(EncodeHintType.MARGIN, 1);
            bitMatrix = writer.encode(text, BarcodeFormat.CODE_128, 200, 50, hintMap);

            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", os);

            log.debug("generated code file");
        } catch (IOException | WriterException e) {
            log.error(e.getMessage(), e);
        }
    }

}
