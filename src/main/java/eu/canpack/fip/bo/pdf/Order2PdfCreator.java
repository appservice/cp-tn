package eu.canpack.fip.bo.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.estimation.dto.EstimationCreateDTO;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.OrderRepository;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Map;
import java.util.stream.Collectors;

import static eu.canpack.fip.bo.pdf.Order2PdfCreatorStyle.*;
import static eu.canpack.fip.bo.pdf.PdfUtil.formatDate;


/**
 * CP S.A.
 * Created by lukasz.mochel on 20.08.2017.
 */
@Service
public class Order2PdfCreator {
    private static BaseFont baseFont;
    private static final Logger log= LoggerFactory.getLogger(Order2PdfCreator.class);




    private static final String FOOTER_TEXT="CAN-PACK FOOD and INDUSTRIAL PACKAGING sp. z o. o.\n"+
        "39-200 Dębica, Mościckiego 23\n"+
        "Tel.: +48 14 670 28 11, E-mail: officecpfip@canpack.eu\n"+
        "Sąd Rejonowy w Rzeszowie, XII Wydział Gospodarczy KRS: 0000228547\n"+
        "Wysokość kapitału zakładowego: 75 853 000,00 PLN\n"+
        "REGON 180017966, NIP 872 222 84 78\n";//+
//        "Konto bankowe: 05 1060 0076 0000 3210 0019 2932 (PLN), 79 1060 0076 0000 3210 0019 2958 (EUR)\n";

    private final OrderRepository orderRepository;

    public Order2PdfCreator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        baseFont = PdfUtil.getArialUnicodeBaseFont();

    }

    // create cells
    @Transactional(readOnly = true)
    public void createPdf(OrderDTO orderDTO, OutputStream os) {
        Order order=orderRepository.findOne(orderDTO.getId());
        Map<Long, EstimationCreateDTO> estimationCreateDTOMap=orderDTO.getEstimations().stream().collect(Collectors.toMap(EstimationCreateDTO::getId, e->e));


        Document doc = new Document(PageSize.A4);

        try {
            PdfWriter writer = PdfWriter.getInstance(doc, os);


            doc.open();
            doc.setMargins(500f, 100f, 100f, 100f);
//            doc.add(new Paragraph(text) );
//                Image image=Image.getInstance("code128.png");
//                doc.add(image);
            doc.add(getImageLogo(doc.getPageSize()));

            OrderPageFooter orderPageFooter = new OrderPageFooter(FOOTER_TEXT);
            writer.setPageEvent(orderPageFooter);


            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));

            Font orderNumberFont = new Font(baseFont, 16, Font.BOLD, Color.BLACK);

            Paragraph orderNumberPgh = new Paragraph("Oferta nr: "+order.getInternalNumber(), orderNumberFont);
            orderNumberPgh.setAlignment(Element.ALIGN_CENTER);
            doc.add(orderNumberPgh);




            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
//            doc.add(new Paragraph(" "));
            PdfPTable table1 = crateTable1(order);
            doc.add(table1);

            doc.add(new Paragraph(" "));

            PdfPTable table2 = createTable2(order,estimationCreateDTOMap);

            doc.add(table2);

            doc.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }



    private PdfPTable crateTable1(Order order) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(90);
        table.setWidths(new float[]{3f, 3f,3f});
        PdfPCell cell1 = createLabel2Cell("Klient");
//        cell1.setBorderWidthLeft(1f);
//        cell1.setBorderWidthRight(1f);
        table.addCell(cell1);


        table.addCell(createLabel2Cell("Numer zapytania"));
        table.addCell(createLabel2Cell("Data"));

//        PdfPCell cell3 = createLabel2Cell("");
//        cell3.setBorderWidthLeft(1f);
//        cell3.setBorderWidthRight(1f);
//        table.addCell(cell3);

        table.addCell(createValue2Cell(order.getClient().getName()));

        if(order.getReferenceNumber()!=null){
            table.addCell(createValue2Cell(order.getReferenceNumber()));

        }else{
            table.addCell(createValue2Cell(""));

        }
        if(order.getCreatedAt()!=null){
            table.addCell(createValue2Cell(formatDate(order.getCreatedAt())));

        }else{
            table.addCell(createValue2Cell(""));
        }
//        table.addCell("");
//        table.addCell(createValue2Cell("Magazynowe"));


        return table;
    }

    private PdfPTable createTable2(Order order,Map<Long,EstimationCreateDTO> estimationCreateDTOMap) throws DocumentException {



        // create 6 column table
        PdfPTable table = new PdfPTable(6);

        // set the width of the table to 100% of page
        table.setWidthPercentage(90);

        // set relative columns width
        table.setWidths(new float[]{0.4f, 3f, 1f, 0.7f, 1f, 1f});


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
        table.addCell(createLabelCell("Ilość [SZT]"));
        table.addCell(createLabelCell("Cena/SZT [PLN]"));
        table.addCell(createLabelCell("Termin wyk."));


        int lp = 1;
        for (Estimation estimation : order.getEstimations()) {
            EstimationCreateDTO edto=estimationCreateDTOMap.get(estimation.getId());
            if(!edto.isChecked()){
                continue;
            }

            table.addCell(createValueCell(String.valueOf(lp),Element.ALIGN_CENTER));
            table.addCell(createValueCell(estimation.getItemName(),Element.ALIGN_LEFT));
            table.addCell(createValueCell(estimation.getItemNumber(),Element.ALIGN_CENTER));
            table.addCell(createValueCell(String.valueOf(estimation.getAmount()),Element.ALIGN_CENTER));
            if(estimation.getEstimatedCost()!=null){
                table.addCell(createValueCell(getFormattedPrice(estimation.getEstimatedCost().setScale(2, BigDecimal.ROUND_HALF_UP)),Element.ALIGN_RIGHT));

            }else{
                table.addCell(createValueCell(" ", Element.ALIGN_RIGHT));

            }

            log.info("object {}", estimation.getEstimatedRealizationDate());
            if(estimation.getEstimatedRealizationDate()!=null){
                table.addCell(createValueCell(formatDate(estimation.getEstimatedRealizationDate()),Element.ALIGN_CENTER));//

            }else{
                table.addCell(createValueCell(" ", Element.ALIGN_RIGHT));

            }
            lp++;
        }

        // empty Rows
//        for (int i = lp; i < 15; i++) {
//            table.addCell(createValueCell(String.valueOf(i)));
//            table.addCell(createValueCell(""));
//            table.addCell(createValueCell(""));
//            table.addCell(createValueCell(""));
//            table.addCell(createValueCell(""));
//            table.addCell(createValueCell(""));
//        }


        return table;


    }

    private String getFormattedPrice(BigDecimal price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format(price);
    }

    private Image getImageLogo(Rectangle documentSize) {
        try {
            Image image = Image.getInstance(this.getClass().getResource("/images/papier_firmowy.jpg"));
            image.setAbsolutePosition(0,0);
            image.scaleAbsolute(documentSize.getWidth(),documentSize.getHeight());
           // image.scalePercent(60);
            return image;

        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
