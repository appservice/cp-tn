package eu.canpack.fip.bo.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * CP S.A.
 * Created by lukasz.mochel on 01.10.2017.
 */
@Service
@Transactional
public class PdfUtilService {

    private static final Logger log = LoggerFactory.getLogger(PdfUtilService.class);


    /**
     * Merge multiple pdf into one pdf
     *

     *            of pdf input stream
     * @param outputStream
     *            output file output stream
     * @throws DocumentException
     * @throws IOException
     */
    public  void margePdfs(OutputStream outputStream, ByteArrayOutputStream ... pdfToMargeStreams)
        throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();


        for (ByteArrayOutputStream os : pdfToMargeStreams) {
            PdfReader reader = new PdfReader(os.toByteArray());
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
                os.close();
//                os.close();
            }
        }

        outputStream.flush();
        document.close();
        outputStream.close();
    }
}

