package eu.canpack.fip.bo.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.*;

import java.awt.*;

/**
 * CP S.A.
 * Created by lukasz.mochel on 21.08.2017.
 */


class OrderPageFooter extends PdfPageEventHelper {
    /** The template with the total number of pages. */
//    PdfTemplate total;
    BaseFont baseFont=PdfUtil.getArialUnicodeBaseFont();

    private Font normal, normalSmall;
    private String text;
    private PdfTemplate total;
//    private


    public OrderPageFooter(String text){
        try{
            this.normal = new Font(baseFont, 10);
            this.normalSmall = new Font(baseFont, 10);
            this.text=text;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * Creates the PdfTemplate that will hold the total number of pages.
//     */
//    public void onOpenDocument(PdfWriter writer, Document document) {
//        total = writer.getDirectContent().createTemplate(30, 22);
//    }

    /**
     * Adds a header to every page
     */
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();


        PdfPTable table = new PdfPTable(1);

          table.getDefaultCell().setFixedHeight(20);
            PdfPCell cell = new PdfPCell();
            cell.setBorder (0);
            cell.setBorderWidthTop(1.5f);
            cell.setBorderColorTop(new Color(199,60,50));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPhrase(new Phrase(text, normalSmall));
            table.addCell(cell);
        table.setTotalWidth(document.getPageSize().getWidth()
                                    - document.leftMargin() - document.rightMargin());
        table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin()-5f, cb);

    }


}
