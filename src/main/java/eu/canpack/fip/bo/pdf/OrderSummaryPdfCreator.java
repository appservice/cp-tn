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
import java.time.format.DateTimeFormatter;

import static eu.canpack.fip.bo.pdf.PdfUtil.formatDate;

/**
 * CP S.A.
 * Created by lukasz.mochel on 20.08.2017.
 */
@Service
@Transactional(readOnly = true)
public class OrderSummaryPdfCreator {
    private static final Logger log = LoggerFactory.getLogger(OrderSummaryPdfCreator.class);


    private static BaseFont baseFont;
    private final BarcodeCreatorService barcodeCreatorService;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public OrderSummaryPdfCreator(BarcodeCreatorService barcodeCreatorService) {
        this.barcodeCreatorService = barcodeCreatorService;
        baseFont = PdfUtil.getArialUnicodeBaseFont();
    }


    public void createPdf(Order order, OutputStream os) {
        Document doc = new Document(PageSize.A4);
        try {
            PdfWriter writer = PdfWriter.getInstance(doc, os);
            PageNumeration event = new PageNumeration("Zlecenie " + order.getInternalNumber());
            writer.setPageEvent(event);

            doc.open();
            doc.add(PdfUtil.getImageLogo(doc.getPageSize()));

     /*       Paragraph dateParagraph = new Paragraph(), dateFont);
            doc.add(dateParagraph);*/
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

            for (Estimation estimation : order.getEstimations()) {

                doc.add(createParagraphWith2Fonts("Przedmiot: ", estimation.getItemName()));

                if (estimation.getItemNumber() != null) {
                    doc.add(createParagraphWith2Fonts("Nr rys.: ", estimation.getItemNumber()));
                }
                doc.add(createParagraphWith2Fonts("Termin realiz: ", estimation.getEstimatedRealizationDate().format(dateFormatter)));

                doc.add(new Paragraph(" "));
                PdfPTable pdfPTable = prepareTableOfOperation(estimation);

                doc.add(pdfPTable);
                doc.newPage();
//                doc.add(new Paragraph(" "));
//                doc.add(new Paragraph(" "));


            }


//
//            PdfPTable pdfPTable = new PdfPTable(new float[]{1,1});
//            pdfPTable.setWidthPercentage(100);
//            if(estimation.getCreatedBy()!=null){
//                pdfPTable.addCell(createTechnologyCreatorCells("Autor technologi: "+estimation.getCreatedBy().getFirstName()+" "+estimation.getCreatedBy().getLastName()));
//
//            }else{
//                pdfPTable.addCell(createTechnologyCreatorCells("Autor technologi:"));
//            }
//
//            pdfPTable.addCell(createTechnologyCreatorCells2("Data opracowania: "+formatDate(estimation.getCreatedAt())));
//            doc.add(pdfPTable);
//
////            doc.add(new Paragraph("Autor technologi: "+estimation.getCreatedBy().getFirstName()+" "+estimation.getCreatedBy().getLastName(),dateFont));
//
//            Paragraph emptyParagaph = new Paragraph("");
//            doc.add(emptyParagaph);
//
//
//            Font fontTitle = new Font(baseFont, 16, Font.BOLD, Color.BLACK);
//            Paragraph titleParagraph = new Paragraph("Karta obiegowa", fontTitle);
//            titleParagraph.setAlignment(Element.ALIGN_CENTER);
//
//            Paragraph drawingNumberParagraph = new Paragraph("Nr rysunku: " + estimation.getItemNumber(), fontTitle);
//            drawingNumberParagraph.setAlignment(Element.ALIGN_CENTER);
//            addEmptyLine(drawingNumberParagraph, 2);
//
//            doc.add(titleParagraph);
//
//            doc.add(drawingNumberParagraph);
//
//            doc.add(crateTable1(estimation));
//            doc.add(new Paragraph(" "));
//
//            doc.add(createTable2(estimation));

            doc.close();

        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
        }
    }


    private PdfPTable prepareTableOfOperation(Estimation estimation) {
        PdfPTable pdfPTable = new PdfPTable(new float[]{1, 4, 1});
        pdfPTable.setWidthPercentage(100);
        pdfPTable.addCell(createLabelCell("Lp"));
        pdfPTable.addCell(createLabelCell("Miejsce operacji"));
        pdfPTable.addCell(createLabelCell("Ilość godz."));
        BigDecimal numberOfHour=BigDecimal.ZERO;
        for (Operation op : estimation.getOperations()) {
            PdfPCell cell = createValueCell(String.valueOf(op.getSequenceNumber()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(cell);

            pdfPTable.addCell(createValueCell(op.getMachine().getName()));
            cell = createValueCell(op.getEstimatedTime().multiply(new BigDecimal(estimation.getAmount())).toString().replace(".",","));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(cell);
           numberOfHour= numberOfHour.add(op.getEstimatedTime());
        }

        Font font = new Font(baseFont, 12, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase("Suma: ",font));
        cell.setColspan(2);
        cell.setPaddingBottom(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPTable.addCell(cell);

        cell=new PdfPCell(new Phrase(numberOfHour.toString().replace(".",","),font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        pdfPTable.addCell(cell);

        return pdfPTable;
    }


    // create cells


    Paragraph createParagraphWith2Fonts(String text1, String text2) {
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

}
