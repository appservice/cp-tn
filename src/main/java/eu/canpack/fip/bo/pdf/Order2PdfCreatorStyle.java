package eu.canpack.fip.bo.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.estimation.EstimationCreateDTO;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.OrderRepository;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CP S.A.
 * Created by lukasz.mochel on 20.08.2017.
 */
@Service
public class Order2PdfCreatorStyle {


     static PdfPCell createLabelCell(String text) {
        // font
        Font font = new Font(PdfUtil.getArialUnicodeBaseFont(), 12, Font.BOLD, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
        Style.labelCellStyle(cell);
        return cell;
    }
    // create cells

     static PdfPCell createValueCell(String text,int horizontalAlignment) {
        // font
        Font font = new Font(PdfUtil.getArialUnicodeBaseFont(), 10, Font.NORMAL, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
         // alignment
         cell.setHorizontalAlignment(horizontalAlignment);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

         // padding
         cell.setPaddingTop(5f);
         cell.setPaddingBottom(5f);

         // border
//            cell.setBorder(1);
//            cell.setBorderWidthBottom(0.5f);

         // height
         cell.setMinimumHeight(18f);

        return cell;
    }
    // create cells

     static PdfPCell createValue2Cell(String text) {
        // font
        Font font = new Font(PdfUtil.getArialUnicodeBaseFont(), 10, Font.NORMAL, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
        Style.value2CellStyle(cell);
        return cell;
    }
    // create cells

     static PdfPCell createLabel2Cell(String text) {
        // font
        Font font = new Font(PdfUtil.getArialUnicodeBaseFont(), 12, Font.BOLD, Color.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text, font));

        // set style
        Style.label2CellStyle(cell);
        return cell;
    }



}
