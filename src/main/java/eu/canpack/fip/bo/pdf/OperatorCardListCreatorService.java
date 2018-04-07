package eu.canpack.fip.bo.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import eu.canpack.fip.bo.operator.OperatorDTO;
import eu.canpack.fip.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * CP S.A.
 * Created by lukasz.mochel on 10.10.2017.
 */
@Service
@Transactional
public class OperatorCardListCreatorService {

    private static final Logger log = LoggerFactory.getLogger(OperatorCardListCreatorService.class);
    private static BaseFont baseFont = PdfUtil.getArialUnicodeBaseFont();

    private final BarcodeCreatorService barcodeCreatorService;
    private final ApplicationProperties applicationProperties;

    public OperatorCardListCreatorService(BarcodeCreatorService barcodeCreatorService, ApplicationProperties applicationProperties) {
        this.barcodeCreatorService = barcodeCreatorService;
        this.applicationProperties = applicationProperties;
    }

    public void createOperatorCard(OutputStream os, List<OperatorDTO> operators) {
        Document doc = new Document(PageSize.A4);

        try {
            PdfWriter writer = PdfWriter.getInstance(doc, os);
            doc.open();

            PdfPTable table = new PdfPTable(applicationProperties.getColumnOnPageWithOperatorCard());
            table.setWidthPercentage(100);
            operators.forEach(operator -> {
                PdfPTable tableContent = new PdfPTable(1);
                tableContent.setWidthPercentage(40);
                PdfPCell cell1 = createCell(operator.getFirstName() + " " + operator.getLastName(),16);
                tableContent.addCell(cell1);

                tableContent.addCell(createImageCell(barcodeImage(operator.getCardNumber())));
                tableContent.addCell(createCell(operator.getCardNumber(),12));
                table.addCell(tableContent);
            });

            doc.add(table);

            // doc.setMargins(500f, 100f, 100f, 100f);

            doc.close();
        } catch (DocumentException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private PdfPCell createCell(String text, int fontSize) {
        Font font = new Font(baseFont, fontSize, Font.NORMAL, Color.BLACK);

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
            Image image = Image.getInstance(byteArrayOutputStream.toByteArray());
            image.setWidthPercentage(50);
            return image;

        } catch (BadElementException | IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }


    PdfPCell createImageCell(Image image) {
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
