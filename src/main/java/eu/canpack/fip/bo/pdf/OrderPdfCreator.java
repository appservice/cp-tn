package eu.canpack.fip.bo.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.order.Order;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

/**
 * CP S.A.
 * Created by lukasz.mochel on 20.08.2017.
 */
@Service
public class OrderPdfCreator {



    // create cells
    public void createPdf(Order order, OutputStream os) {
        Document doc = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(doc, os);


            doc.open();
            doc.setMargins(500f, 100f, 100f, 100f);
//            doc.add(new Paragraph(text) );
//                Image image=Image.getInstance("code128.png");
//                doc.add(image);
            doc.add(getImageLogo(doc.getPageSize()));
            doc.add(new Paragraph("                Sekcja Narzędziowa Can-Pack FIP sp. z O.O."));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            PdfPTable table1 = crateTable1(order);
            doc.add(table1);

            doc.add(new Paragraph(" "));

            PdfPTable table2 = createTable2(order);

            doc.add(table2);

            doc.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private static PdfPCell createLabelCell(String text) {
        // font
        Font font = new Font(1, 12, Font.BOLD, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
        Style.labelCellStyle(cell);
        return cell;
    }
    // create cells

    private static PdfPCell createValueCell(String text) {
        // font
        Font font = new Font(1, 10, Font.NORMAL, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
        Style.valueCellStyle(cell);

        return cell;
    }
    // create cells

    private static PdfPCell createValue2Cell(String text) {
        // font
        Font font = new Font(1, 10, Font.NORMAL, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
        Style.value2CellStyle(cell);
        return cell;
    }
    // create cells

    private static PdfPCell createLabel2Cell(String text) {
        // font
        Font font = new Font(1, 12, Font.BOLD, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
        Style.label2CellStyle(cell);
        return cell;
    }

    private PdfPTable crateTable1(Order order) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(90);
        table.setWidths(new float[]{3f, 3f, 3f});
        PdfPCell cell1 = createLabel2Cell("Klient");
//        cell1.setBorderWidthLeft(1f);
//        cell1.setBorderWidthRight(1f);
        table.addCell(cell1);


        table.addCell(createLabel2Cell("Numer zapotrzebowania"));

        PdfPCell cell3 = createLabel2Cell("Typ zamówienia");
//        cell3.setBorderWidthLeft(1f);
//        cell3.setBorderWidthRight(1f);
        table.addCell(cell3);

        table.addCell(createValue2Cell(order.getClient().getName()));
        table.addCell(createValue2Cell(order.getInternalNumber()));
        table.addCell(createValue2Cell("Magazynowe"));


        return table;
    }

    private PdfPTable createTable2(Order order) throws DocumentException {

        // create 6 column table
        PdfPTable table = new PdfPTable(6);

        // set the width of the table to 100% of page
        table.setWidthPercentage(90);

        // set relative columns width
        table.setWidths(new float[]{0.4f, 3f, 0.8f, 0.8f, 1f, 1f});


        // ----------------Table Header "Title"----------------
        // font
//        Font font = new Font(1,14,Font.NORMAL,Color.BLACK);
//        // create header cell
//        PdfPCell cell = new PdfPCell(new Phrase("HMKCODE.com - iText PDFTable Example",font));
//        // set Column span "1 cell = 6 cells width"
//        cell.setColspan(6);
//        // set style
//        Style.headerCellStyle(cell);
//        // add to table
//        table.addCell(cell);

        //-----------------Table Cells Label/Value------------------

        // 1st Row
        table.addCell(createLabelCell("LP"));
        table.addCell(createLabelCell("Nazwa czesci"));
        table.addCell(createLabelCell("Nr rysunku"));
        table.addCell(createLabelCell("Ilosc [SZT]"));
        table.addCell(createLabelCell("Cena [PLN]"));
        table.addCell(createLabelCell("Termin wyk."));


        int lp = 1;
        for (Estimation estimation : order.getEstimations()) {
            table.addCell(createValueCell(String.valueOf(lp)));
            table.addCell(createValueCell(estimation.getDescription()));
            table.addCell(createValueCell(estimation.getDrawing().getNumber()));
            table.addCell(createValueCell(String.valueOf(estimation.getAmount())));
            table.addCell(createValueCell(getFormattedPrice(estimation.getFinalCost().setScale(2, BigDecimal.ROUND_HALF_UP))));
            table.addCell(createValueCell(estimation.getNeededRealizationDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY"))));
            lp++;
        }


        // empty Rows
        for (int i = lp; i < 15; i++) {
            table.addCell(createValueCell(String.valueOf(i)));
            table.addCell(createValueCell(""));
            table.addCell(createValueCell(""));
            table.addCell(createValueCell(""));
            table.addCell(createValueCell(""));
            table.addCell(createValueCell(""));
        }


        return table;


    }

    private String getFormattedPrice(BigDecimal price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format(price);
    }

    private Image getImageLogo(Rectangle documentSize) {
        try {
            Image image = Image.getInstance(this.getClass().getResource("/images/logo.jpg"));
            image.setAbsolutePosition(documentSize.getWidth()-140,documentSize.getHeight()-90);
            image.scalePercent(60);
            return image;

        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
