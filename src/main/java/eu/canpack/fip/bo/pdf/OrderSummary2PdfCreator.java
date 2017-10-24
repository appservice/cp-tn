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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
        Document doc = new Document(PageSize.A4);
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


            doc.add(new Paragraph(" "));

            Paragraph paragraph = createParagraphWith2Fonts("Klient: ", order.getClient().getName());
            paragraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(paragraph);
            if (order.getReferenceNumber() != null) {
                paragraph = createParagraphWith2Fonts("Numer zlec. klienta: ", order.getReferenceNumber());
                paragraph.setAlignment(Element.ALIGN_CENTER);
                doc.add(paragraph);
            }
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));

            int i = 1;
            for (Estimation estimation : estimations) {
                StringBuilder detal = new StringBuilder();
                if (estimation.getItemNumber() != null) {
                    detal.append(estimation.getItemNumber()).append(" ");
                }
                detal.append(estimation.getItemName());

                doc.add(createParagraphWith2Fonts(i + ". Przedmiot: ", detal.toString()));


                StringBuilder paragraph2 = new StringBuilder("     ilość: " + estimation.getAmount() + " szt");
                if (estimation.getEstimatedRealizationDate() != null) {
                    paragraph2.append(", ").append("termin realizacji: ").append(estimation.getEstimatedRealizationDate().format(dateFormatter));
                }
                paragraph2.append(",");
                doc.add(new Paragraph(paragraph2.toString(), new Font(baseFont)));

                if (!estimation.getOperations().isEmpty()) {
                    StringBuilder sb = new StringBuilder("     szacowana ilość roboczogodzin/szt: ");
                    sb.append(formatBigDecimal(calculateWorkingHourPerItem(estimation))).append(" godz., ");
                    sb.append("suma roboczogodzin: ");
                    sb.append(formatBigDecimal(calculateWorkingHourPerItem(estimation)
                                                   .multiply(new BigDecimal(estimation.getAmount()))));
                    sb.append(" godz.");


                    doc.add(new Paragraph(sb.toString(), new Font(baseFont)));
                }

                doc.add(new Paragraph(" "));
//                PdfPTable pdfPTable = prepareTableOfOperation(estimation);

//                doc.add(pdfPTable);
//                doc.newPage();
                i++;

            }

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


    /*private PdfPTable prepareTableOfOperation(Estimation estimation) {
        PdfPTable pdfPTable = new PdfPTable(new float[]{1, 6,1,1});
        pdfPTable.setWidthPercentage(100);
        pdfPTable.addCell(createLabelCell("Lp"));
        pdfPTable.addCell(createLabelCell("Miejsce operacji"));
        pdfPTable.addCell(createLabelCell("Ilość godz./szt"));
        pdfPTable.addCell(createLabelCell("Ilość godz."));
        BigDecimal numberOfHour=BigDecimal.ZERO;
        for (Operation op : estimation.getOperations()) {
            PdfPCell cell = createValueCell(String.valueOf(op.getSequenceNumber()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(cell);

            pdfPTable.addCell(createValueCell(op.getMachine().getName()));

            cell = createValueCell(formatBigDecimal(op.getEstimatedTime()).replace(".",","));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(cell);

            BigDecimal bdMultipled=op.getEstimatedTime().multiply(new BigDecimal(estimation.getAmount()));

            cell = createValueCell(formatBigDecimal(bdMultipled).replace(".",","));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(cell);
           numberOfHour= numberOfHour.add(bdMultipled);
        }

        Font font = new Font(baseFont, 12, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase("Suma: ",font));
        cell.setColspan(3);
        cell.setPaddingBottom(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPTable.addCell(cell);

        cell=new PdfPCell(new Phrase(formatBigDecimal(numberOfHour).replace(".",","),font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        pdfPTable.addCell(cell);

        return pdfPTable;
    }*/


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
        cell.setPaddingBottom(5f);


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
