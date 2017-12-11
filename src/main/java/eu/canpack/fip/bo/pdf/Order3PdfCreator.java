package eu.canpack.fip.bo.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
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
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

import static eu.canpack.fip.bo.pdf.Order2PdfCreatorStyle.*;
import static eu.canpack.fip.bo.pdf.Order2PdfCreatorStyle.createValue2Cell;
import static eu.canpack.fip.bo.pdf.PdfUtil.formatDate;


/**
 * CP S.A.
 * Created by lukasz.mochel on 20.08.2017.
 */
@Service
public class Order3PdfCreator {
    private static final Logger log = LoggerFactory.getLogger(Order3PdfCreator.class);
    private static final String FOOTER_TEXT = "CAN-PACK FOOD and INDUSTRIAL PACKAGING sp. z o. o.\n" +
        "39-200 Dębica, Mościckiego 23\n" +
        "Tel.: +48 14 670 28 11, E-mail: officecpfip@canpack.eu\n" +
        "Sąd Rejonowy w Rzeszowie, XII Wydział Gospodarczy KRS: 0000228547\n" +
        "Wysokość kapitału zakładowego: 75 853 000,00 PLN\n" +
        "REGON 180017966, NIP 872 222 84 78\n";//+
    private static BaseFont baseFont = PdfUtil.getArialUnicodeBaseFont();
    ;
    //        "Konto bankowe: 05 1060 0076 0000 3210 0019 2932 (PLN), 79 1060 0076 0000 3210 0019 2958 (EUR)\n";
    private final OrderRepository orderRepository;

    public Order3PdfCreator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;

    }

    // create cells
    @Transactional(readOnly = true)
    public void createPdf(OrderDTO orderDTO, OutputStream os) {
        Order order = orderRepository.findOne(orderDTO.getId());
        Map<Long, EstimationCreateDTO> estimationCreateDTOMap = orderDTO.getEstimations().stream().collect(Collectors.toMap(EstimationCreateDTO::getId, e -> e));


        Document doc = new Document(PageSize.A4);
        doc.setMargins(25, 25, 25, 80);

        try {
            PdfWriter writer = PdfWriter.getInstance(doc, os);

            OrderPageFooter orderPageFooter = new OrderPageFooter(FOOTER_TEXT);
            writer.setPageEvent(orderPageFooter);
            doc.open();

            doc.add(PdfUtil.getImageLogo(doc.getPageSize()));




            Color companyNameColor = new Color(35, 60, 183);
            doc.add(new Paragraph("Can-Pack Food and Industrial Packaging Sp. z o.o.", new Font(baseFont, 14, Font.BOLD, companyNameColor)));
            doc.add(new Paragraph("Dział Narzędziownia", new Font(baseFont, 14, Font.NORMAL, companyNameColor)));
            Font fontCompanyNameSmaller = new Font(baseFont, 10, Font.NORMAL);
            doc.add(new Paragraph("32-800 Brzesko, ul Starowiejska 28", fontCompanyNameSmaller));
            doc.add(new Paragraph("Tel: +48 14 66 20 101", fontCompanyNameSmaller));
            doc.add(new Paragraph("Fax: +48 14 66 20 101", fontCompanyNameSmaller));

            Font orderNumberFont = new Font(baseFont, 16, Font.BOLD, Color.BLACK);

            doc.add(new Paragraph(" "));
            Paragraph orderNumberPgh = new Paragraph("Oferta nr: " + order.getInternalNumber(), orderNumberFont);
            orderNumberPgh.setAlignment(Element.ALIGN_CENTER);
            doc.add(orderNumberPgh);

            Paragraph orderDateParagraph = new Paragraph(formatDate(LocalDate.now()), new Font(baseFont, 12));
            orderDateParagraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(orderDateParagraph);


            doc.add(new Paragraph(" "));
            PdfPTable table1 = crateTable1(order);
            doc.add(table1);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("W odpowiedzi na zapytanie ofertowe informujemy że cena za wykonanie detali wynosi:", new Font(baseFont, 10, Font.NORMAL)));

            doc.add(new Paragraph(" "));

            PdfPTable table2 = createTable2(order, estimationCreateDTOMap);

            doc.add(table2);
            doc.add(new Paragraph(" "));
            Font remarksFont = new Font(baseFont, 10);

            StringBuilder offerRemarksSb = new StringBuilder("");
            if(order.getOfferRemarks()!=null){
                offerRemarksSb.append(order.getOfferRemarks());
            }


                doc.add(new Paragraph(offerRemarksSb.toString(), remarksFont));

            doc.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    private PdfPTable crateTable1(Order order) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3f, 1f, 3f});
        PdfPCell cell1 = createValue2Cell("Klient: " + order.getClient().getName());
        table.addCell(cell1);
        table.addCell(createValue2Cell(""));
        String creatorName = "";
        if (order.getEstimationMaker() != null) {
            creatorName = order.getEstimationMaker().getFirstName() + " " + order.getEstimationMaker().getLastName();
        }
        table.addCell(createValue2Cell("Ofertę przygotował: " + creatorName));


        String referenceNumber = "";
        if (order.getReferenceNumber() != null) {
            referenceNumber = order.getReferenceNumber();
        }
        table.addCell(createValue2Cell("Nr. ref: " + referenceNumber));
        table.addCell(createValue2Cell(""));

        String email = "";
        if (order.getEstimationMaker() != null) {
            email = order.getEstimationMaker().getEmail();
        }
        table.addCell(createValue2Cell("e-mail: " + email));
        table.addCell(createValue2Cell("Osoba ref: " + order.getCreatedBy().getFirstName() + " " + order.getCreatedBy().getLastName()));
        table.addCell(createValue2Cell(""));

        //phone
        StringBuilder phoneSb = new StringBuilder("tel: ");
        if (order.getCreatedBy().getPhone() != null) {
            phoneSb.append(order.getEstimationMaker().getPhone());
        }
        table.addCell(createValue2Cell(phoneSb.toString()));


        return table;
    }

    private PdfPTable createTable2(Order order, Map<Long, EstimationCreateDTO> estimationCreateDTOMap) throws DocumentException {


        // create 6 column table
        PdfPTable table = new PdfPTable(7);

        // set the width of the table to 100% of page
        table.setWidthPercentage(100);

        // set relative columns width
        table.setWidths(new float[]{0.4f, 3f, 1.2f, 0.7f, 1f, 1f, 1f});


        //-----------------Table Cells Label/Value------------------

        // 1st Row
        table.addCell(createLabelCell("LP"));
        table.addCell(createLabelCell("Nazwa"));
        table.addCell(createLabelCell("Nr rysunku"));
        table.addCell(createLabelCell("Ilość szt"));
        table.addCell(createLabelCell("Data realizacji"));
        table.addCell(createLabelCell("Cena netto/szt"));
        table.addCell(createLabelCell("Wartość netto"));


        int lp = 1;
        BigDecimal totalCost = BigDecimal.ZERO;
        for (Estimation estimation : order.getEstimations()) {
            EstimationCreateDTO edto = estimationCreateDTOMap.get(estimation.getId());
            if (!edto.isChecked()) {
                continue;
            }

            table.addCell(createValueCell(String.valueOf(lp), Element.ALIGN_CENTER));
            table.addCell(createValueCell(estimation.getItemName(), Element.ALIGN_LEFT));
            table.addCell(createValueCell(estimation.getItemNumber(), Element.ALIGN_LEFT));
            table.addCell(createValueCell(String.valueOf(estimation.getAmount()), Element.ALIGN_CENTER));

            log.info("object {}", estimation.getEstimatedRealizationDate());
            if (estimation.getEstimatedRealizationDate() != null) {
                table.addCell(createValueCell(formatDate(estimation.getEstimatedRealizationDate()), Element.ALIGN_CENTER));//

            } else {
                table.addCell(createValueCell(" ", Element.ALIGN_RIGHT));

            }
            if (estimation.getEstimatedCost() != null) {
                BigDecimal value = estimation.getEstimatedCost().multiply(new BigDecimal(estimation.getAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
                table.addCell(createValueCell(getFormattedPrice(estimation.getEstimatedCost().setScale(2, BigDecimal.ROUND_HALF_UP)), Element.ALIGN_RIGHT));
                table.addCell(createValueCell(getFormattedPrice(value), Element.ALIGN_RIGHT));
                totalCost = totalCost.add(value);

            } else {
                table.addCell(createValueCell(" ", Element.ALIGN_RIGHT));
                table.addCell(createValueCell(" ", Element.ALIGN_RIGHT));

            }


            lp++;
        }

        table.addCell(cprepareCellWithoutBorder(""));
        table.addCell(cprepareCellWithoutBorder(""));
        table.addCell(cprepareCellWithoutBorder(""));
        table.addCell(cprepareCellWithoutBorder(""));
        table.addCell(cprepareCellWithoutBorder(""));
        table.addCell(cprepareCellWithoutBorder("Suma:"));

        table.addCell(cprepareCellWithoutBorder(getFormattedPrice(totalCost.setScale(2, BigDecimal.ROUND_HALF_UP))));

        return table;


    }

    private String getFormattedPrice(BigDecimal price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format(price);
    }


    PdfPCell cprepareCellWithoutBorder(String text) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(text, new Font(baseFont, 10, Font.BOLD)));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setBorderWidth(0);

        return pdfPCell;
    }
}
