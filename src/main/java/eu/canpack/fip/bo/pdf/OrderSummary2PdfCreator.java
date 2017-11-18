package eu.canpack.fip.bo.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.List;


/**
 * CP S.A.
 * Created by lukasz.mochel on 20.08.2017.
 */
@Service
@Transactional(readOnly = true)
public class OrderSummary2PdfCreator {
    private static final Logger log = LoggerFactory.getLogger(OrderSummary2PdfCreator.class);


    private static BaseFont baseFont;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public OrderSummary2PdfCreator() {
        baseFont = PdfUtil.getArialUnicodeBaseFont();
    }


    public void createPdf(Order order, List<Estimation> estimations, OutputStream os) {
        Document doc = new Document(PageSize.A4.rotate());
        try {

            PdfWriter writer = PdfWriter.getInstance(doc, os);
            PageNumeration event = new PageNumeration("Zlecenie " + order.getInternalNumber());
            writer.setPageEvent(event);

            doc.open();
            doc.add(PdfUtil.getImageLogo(doc.getPageSize()));

            Font orderNumberFont = new Font(baseFont, 18, Font.BOLD, Color.BLACK);


            Paragraph orderNumberPgh = new Paragraph("Zlecenie nr: " + order.getInternalNumber(), orderNumberFont);
            orderNumberPgh.setAlignment(Element.ALIGN_CENTER);
            doc.add(orderNumberPgh);


            doc.add(new Paragraph(" ", new Font(baseFont)));

            Paragraph paragraph = createParagraphWith2Fonts("Klient: ", order.getClient().getName());
            paragraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(paragraph);
            if (order.getReferenceNumber() != null) {
                paragraph = createParagraphWith2Fonts("Numer zlec. klienta: ", order.getReferenceNumber());
                paragraph.setAlignment(Element.ALIGN_CENTER);
                doc.add(paragraph);
            }
            if(order.getDescription()!=null){
                paragraph = createParagraphWith2Fonts("Uwagi: ", order.getDescription());
                paragraph.setAlignment(Element.ALIGN_CENTER);
                doc.add(paragraph);
            }
            doc.add(new Paragraph(" "));


            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Data wydruku: " + LocalDate.now().format(dateFormatter)));
            doc.add(new Paragraph(" "));

            doc.add(prepareTableOfOperation(estimations));


            doc.close();

        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
        }
    }


    BigDecimal calculateWorkingHourPerItem(Estimation estimation) {
        return estimation.getOperations()
            .stream().map(Operation::getEstimatedTime)
            .reduce((item, accum) -> accum.add(item))
            .orElse(BigDecimal.ZERO);
    }


    private PdfPTable prepareTableOfOperation(List<Estimation> estimations) {
        PdfPTable pdfPTable = new PdfPTable(new float[]{0.5f, 5, 1,1.8f, 1.5f, 1, 1, 1.5f, 1.5f});
        pdfPTable.setWidthPercentage(100);
        pdfPTable.addCell(createLabelCell(""));
        pdfPTable.addCell(createLabelCell("Przedmiot"));
        pdfPTable.addCell(createLabelCell("Ilość"));
        pdfPTable.addCell(createLabelCell("MPK/Index"));
        pdfPTable.addCell(createLabelCell("Termin wyk."));
        pdfPTable.addCell(createLabelCell("Rbh/szt"));
        pdfPTable.addCell(createLabelCell("\u2211 Rbh"));
        pdfPTable.addCell(createLabelCell("Data odb."));
        pdfPTable.addCell(createLabelCell("Odebrał"));


        int counter = 1;
        for (Estimation estimation : estimations) {


            PdfPCell cell;

            //LP
            cell = createValueCell(String.valueOf(counter));
            pdfPTable.addCell(cell);

            //nazwa
            StringBuilder detal = new StringBuilder();
            if (estimation.getItemNumber() != null) {
                detal.append(estimation.getItemNumber()).append(" ");
            }
            detal.append(estimation.getItemName());

            cell = createValueCell(detal.toString());
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pdfPTable.addCell(cell);

            //ilość
            cell = createValueCell(String.valueOf(estimation.getAmount()));
            pdfPTable.addCell(cell);

            //ilość
            StringBuilder mpk = new StringBuilder("");
            if(estimation.getMpk()!=null){
                mpk.append(estimation.getMpk());
            }
            cell = createValueCell(mpk.toString());
            pdfPTable.addCell(cell);

            //data realizacji
            StringBuilder amountSb = new StringBuilder("");
            if (estimation.getEstimatedRealizationDate() != null) {
                amountSb.append(estimation.getEstimatedRealizationDate().format(dateFormatter));
            }
            cell = createValueCell(amountSb.toString());
            pdfPTable.addCell(cell);

            //if emergency order then not fill rbh fields
            if (estimation.getOrder().getOrderType() != OrderType.EMERGENCY) {
                cell = createValueCell(formatBigDecimal(calculateWorkingHourPerItem(estimation)));
                pdfPTable.addCell(cell);

                BigDecimal sumOfWhorkingHours = calculateWorkingHourPerItem(estimation).multiply(new BigDecimal(estimation.getAmount()));
                cell = createValueCell(formatBigDecimal(sumOfWhorkingHours));
                pdfPTable.addCell(cell);
            } else {
                pdfPTable.addCell("");
                pdfPTable.addCell("");
            }
            pdfPTable.addCell("");
            pdfPTable.addCell("");

            counter++;
        }


        return pdfPTable;
    }


    // create cells


    private Paragraph createParagraphWith2Fonts(String text1, String text2) {
        Phrase phrase = new Phrase();
        Chunk chunk = new Chunk(text1, new Font(baseFont, 14, Font.NORMAL));
        phrase.add(chunk);
        chunk = new Chunk(text2, new Font(baseFont, 14, Font.BOLD));
        phrase.add(chunk);

        return new Paragraph(phrase);
    }


    private PdfPCell createLabelCell(String text) {
        // font
        Font font = new Font(baseFont, 12, Font.BOLD, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPaddingBottom(5f);
        cell.setBackgroundColor(Color.LIGHT_GRAY);

        // set style
//        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        return cell;
    }

    private PdfPCell createValueCell(String text) {
        // font
        Font font = new Font(baseFont, 12, Font.NORMAL, Color.BLACK);

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5f);
        cell.setPaddingBottom(10f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        return cell;
    }


    private String formatBigDecimal(BigDecimal inputBigDecimal) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3); //Sets the maximum number of digits after the decimal point
        df.setMinimumFractionDigits(0); //Sets the minimum number of digits after the decimal point
        df.setGroupingUsed(false); //If false thousands separator such ad 1,000 wont work so it will display 1000

        return df.format(inputBigDecimal);

    }
}
