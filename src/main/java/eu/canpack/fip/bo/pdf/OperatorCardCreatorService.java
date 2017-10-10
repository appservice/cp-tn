package eu.canpack.fip.bo.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import eu.canpack.fip.domain.Operator;
import eu.canpack.fip.service.dto.OperatorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * CP S.A.
 * Created by lukasz.mochel on 10.10.2017.
 */
@Service
@Transactional
public class OperatorCardCreatorService {

    private static final Logger log = LoggerFactory.getLogger(OperatorCardCreatorService.class);
   private static BaseFont baseFont = PdfUtil.getArialUnicodeBaseFont();

   private final BarcodeCreatorService barcodeCreatorService;

    public OperatorCardCreatorService(BarcodeCreatorService barcodeCreatorService) {
        this.barcodeCreatorService = barcodeCreatorService;
    }

    public void createOperatorCard(OutputStream os, OperatorDTO operator){
        Document doc = new Document(PageSize.A4);

        try {
            PdfWriter writer = PdfWriter.getInstance(doc, os);
            doc.open();

            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(40);
            PdfPTable tableContent = new PdfPTable(1);
            tableContent.setWidthPercentage(100);
            PdfPCell cell1=createCell(operator.getFirstName() + " " + operator.getLastName());


            tableContent.addCell(cell1);
            tableContent.addCell(createCell(operator.getCardNumber()));

            tableContent.addCell(createImageCell(barcodeImage(operator.getCardNumber())));
            table.addCell(tableContent);

            doc.add(table);

           // doc.setMargins(500f, 100f, 100f, 100f);

            doc.close();
        }catch (DocumentException  ex){
            log.error(ex.getMessage(),ex);
        }
    }

    private PdfPCell createCell(String text){
        Font font = new Font(baseFont, 16, Font.NORMAL, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        cell.setBorderWidth(0);
        return cell;
    }
    private Image barcodeImage(String cardNumber) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            barcodeCreatorService.createBarcode(cardNumber, byteArrayOutputStream);
            Image image=Image.getInstance(byteArrayOutputStream.toByteArray());
            image.setWidthPercentage(50);
            return image;

        } catch (BadElementException | IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }


     PdfPCell createImageCell(Image image){
        PdfPCell imageCell = new PdfPCell();
        imageCell.setBorderWidth(0);

        imageCell.setPadding(7f);
        imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        image.setAlignment(Element.ALIGN_CENTER);
        image.setWidthPercentage(80);
         imageCell.addElement(image);

         return imageCell;
    }
}
