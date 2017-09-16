package eu.canpack.fip.bo.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

/**
 * CP S.A.
 * Created by lukasz.mochel on 21.08.2017.
 */


class PageNumeration extends PdfPageEventHelper {
    /**
     * The template with the total number of pages.
     */
    private PdfTemplate total;
    private BaseFont baseFont = PdfUtil.getArialUnicodeBaseFont();

    private Font normal, normalSmall;
    private String text;


    public PageNumeration(String text) {
        try {
            this.normal = new Font(baseFont, 10);
            this.normalSmall = new Font(baseFont, 10);
            this.text = text;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the PdfTemplate that will hold the total number of pages.
     */
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 12);
    }

    /**
     * Adds a header to every page
     */
    public void onEndPage(PdfWriter writer, Document document) {
       // createHeader(writer, document, "Przykładowy header");

        PdfPTable table = new PdfPTable(3);
        try {
            table.setWidths(new int[]{24, 24, 2});
            table.getDefaultCell().setFixedHeight(10);
            table.getDefaultCell().setBorder(Rectangle.TOP);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBorderWidthTop(1);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPhrase(new Phrase(text, normalSmall));
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBorderWidthTop(1);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPhrase(new Phrase(String.format("Strona %d z", writer.getPageNumber()), normal));
            table.addCell(cell);

            cell = new PdfPCell(Image.getInstance(total));
            cell.setBorder(0);
            cell.setBorderWidthTop(1);
            table.addCell(cell);
            table.setTotalWidth(document.getPageSize().getWidth()
                                    - document.leftMargin() - document.rightMargin());
            table.writeSelectedRows(0, -1, document.leftMargin(),
                                    document.bottomMargin(), writer.getDirectContent());
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    private void createHeader(PdfWriter writer, Document document, String headerText) {
        PdfPTable header = new PdfPTable(1);
        PdfPCell headerCell = new PdfPCell();
        // headerCell.setBorder(0);
        headerCell.setBorderWidthBottom(1);
        headerCell.setBorder(Rectangle.BOTTOM);
        headerCell.setPhrase(new Phrase(headerText, normalSmall));

        header.addCell(headerCell);
//        header.setWidthPercentage(100);
        header.getDefaultCell().setBorder(Rectangle.BOTTOM);
        header.getDefaultCell().setFixedHeight(10);
        header.setTotalWidth(document.getPageSize().getWidth()
                                 - document.leftMargin() - document.rightMargin());
        header.writeSelectedRows(0, -1, document.leftMargin(), document.getPageSize().getHeight() - 20, writer.getDirectContent());
    }

    /**
     * Fills out the total number of pages before the document is closed.
     */
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                                   new Phrase(String.valueOf(writer.getPageNumber() - 1), normal),
                                   2, 0, 0);
    }
}
