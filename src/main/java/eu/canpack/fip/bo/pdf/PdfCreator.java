package eu.canpack.fip.bo.pdf;


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * CP S.A.
 * Created by lukasz.mochel on 19.07.2017.
 */
public class PdfCreator {

    /** Path to the resulting PDF file. */
    public static final String RESULT        = "hello.pdf";

    public void createPdf(String text){
        Document doc=new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(RESULT));

            doc.open();
            doc.add(new Paragraph(text) );
            try {
                Image image=Image.getInstance("code128.png");
                doc.add(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
            doc.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
