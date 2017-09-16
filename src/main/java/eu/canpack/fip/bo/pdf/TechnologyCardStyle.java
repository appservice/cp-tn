package eu.canpack.fip.bo.pdf;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CP S.A.
 * Created by lukasz.mochel on 24.08.2017.
 */
public class TechnologyCardStyle {

    private static BaseFont baseFont = PdfUtil.getArialUnicodeBaseFont();




    static PdfPCell createLabelCell(String text) {
        // font
        Font font = new Font(baseFont, 14, Font.BOLD, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        return cell;
    }

    static PdfPCell createValue2Cell(String text) {
        // font
        Font font = new Font(baseFont, 14, Font.NORMAL, Color.BLACK);


        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);


        return cell;
    }

    static PdfPCell createValueCell(String text) {
        // font
        Font font = new Font(1, 12, Font.NORMAL, Color.BLACK);

        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        return cell;
    }

    // create cells
    static PdfPCell createLabel2Cell(String text) {
        // font
        Font font = new Font(baseFont, 12, Font.NORMAL, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5f);

        // set style
//        Style.label2CellStyle(cell);
        return cell;
    }

    static PdfPCell createNestedTableCellWithoutBorder(String text) {
        // font
        Font font = new Font(baseFont, 12, Font.NORMAL, Color.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(10f);

        cell.setBorder(0);
        return cell;
    }

    static PdfPCell createNestedTableHeaderCell(String text) {
        // font
        Font font = new Font(baseFont, 12, Font.NORMAL, Color.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(10f);
        return cell;
    }

    static PdfPCell createColspannedCell(String text){
        Font font = new Font(baseFont, 12, Font.NORMAL, Color.BLACK);
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBorder(0);
        pdfPCell.setPhrase(new Phrase(text, font));
        pdfPCell.setColspan(2);
        pdfPCell.setPadding(5f);
        return pdfPCell;
    }

    static PdfPCell createImageCell(Image image){
        PdfPCell imageCell = new PdfPCell();
        imageCell.setBorder(0);
        imageCell.addElement(image);
        imageCell.setPadding(7f);
        return imageCell;
    }

    static PdfPCell createTechnologyCreatorCells(String text){
        Font font = new Font(baseFont, 12, Font.NORMAL, Color.BLACK);
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBorder(Rectangle.BOTTOM);



        pdfPCell.setPhrase(new Phrase(text, font));
//        pdfPCell.setPadding(5f);
        pdfPCell.setPaddingLeft(0);
        pdfPCell.setPaddingTop(8f);
        pdfPCell.setPaddingBottom(5f);
        return pdfPCell;
    }

    static PdfPCell createTechnologyCreatorCells2(String text){
        PdfPCell pdfPCell = createTechnologyCreatorCells(text);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return pdfPCell;
    }


    static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


}
