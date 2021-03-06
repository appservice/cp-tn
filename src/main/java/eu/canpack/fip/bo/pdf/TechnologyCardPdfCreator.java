package eu.canpack.fip.bo.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.operation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Comparator;

import static eu.canpack.fip.bo.pdf.PdfUtil.formatDate;
import static eu.canpack.fip.bo.pdf.TechnologyCardStyle.*;

/**
 * CP S.A.
 * Created by lukasz.mochel on 20.08.2017.
 */
@Service
@Transactional(readOnly = true)
public class TechnologyCardPdfCreator {
    private static final Logger log = LoggerFactory.getLogger(TechnologyCardPdfCreator.class);


    private static BaseFont baseFont;
    private final BarcodeCreatorService barcodeCreatorService;

    public TechnologyCardPdfCreator(BarcodeCreatorService barcodeCreatorService) {
        this.barcodeCreatorService = barcodeCreatorService;
        baseFont = PdfUtil.getArialUnicodeBaseFont();
    }


    public void createPdf(Estimation estimation, OutputStream os) {
        Document doc = new Document(PageSize.A4);
        try {
            PdfWriter writer = PdfWriter.getInstance(doc, os);
            PageNumeration event = new PageNumeration(estimation.getItemNumber());
            writer.setPageEvent(event);

            doc.open();
            doc.add(PdfUtil.getImageLogo(doc.getPageSize()));


            Font orderNumberFont = new Font(baseFont, 16, Font.BOLD, Color.BLACK);

            Paragraph orderNumberPgh = new Paragraph("Technologia wykonania do zlecenia nr: ", orderNumberFont);
            orderNumberPgh.setAlignment(Element.ALIGN_CENTER);
            doc.add(orderNumberPgh);
            Paragraph orderNumberPgh2 = new Paragraph(estimation.getOrder().getInternalNumber(), orderNumberFont);
            orderNumberPgh2.setAlignment(Element.ALIGN_CENTER);
            doc.add(orderNumberPgh2);

            doc.add(new Paragraph(" "));

            Font dateFont = new Font(baseFont, 14, Font.NORMAL, Color.BLACK);

           if(estimation.getSapNumber()!=null){
                doc.add(new Paragraph("Numer zlecenia SAP: "+estimation.getSapNumber(),dateFont));
            }
            doc.add(new Paragraph("Klient: " + estimation.getOrder().getClient().getName(), dateFont));
            String referenceClientNumber="Numer zlec. klienta:";
            if(estimation.getOrder().getReferenceNumber()!=null){
                referenceClientNumber=referenceClientNumber+" "+estimation.getOrder().getReferenceNumber();
            }
            doc.add(new Paragraph(referenceClientNumber, dateFont));

            PdfPTable pdfPTable = new PdfPTable(new float[]{1,1});
            pdfPTable.setWidthPercentage(100);
            if(estimation.getCreatedBy()!=null){
                pdfPTable.addCell(createTechnologyCreatorCells("Autor technologi: "+estimation.getCreatedBy().getFirstName()+" "+estimation.getCreatedBy().getLastName()));

            }else{
                pdfPTable.addCell(createTechnologyCreatorCells("Autor technologi:"));
            }

            pdfPTable.addCell(createTechnologyCreatorCells2("Data opracowania: "+formatDate(estimation.getCreatedAt())));
            doc.add(pdfPTable);

//            doc.add(new Paragraph("Autor technologi: "+estimation.getCreatedBy().getFirstName()+" "+estimation.getCreatedBy().getLastName(),dateFont));

            Paragraph emptyParagaph = new Paragraph("");
            doc.add(emptyParagaph);


            Font fontTitle = new Font(baseFont, 16, Font.BOLD, Color.BLACK);
            Paragraph titleParagraph = new Paragraph("Karta obiegowa", fontTitle);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(titleParagraph);

            if(estimation.getItemNumber()!=null){
                Paragraph drawingNumberParagraph = new Paragraph("Nr rysunku: " + estimation.getItemNumber(), fontTitle);
                drawingNumberParagraph.setAlignment(Element.ALIGN_CENTER);
                addEmptyLine(drawingNumberParagraph, 2);
                doc.add(drawingNumberParagraph);
            }


            doc.add(crateTable1(estimation));
            doc.add(new Paragraph(" "));

            doc.add(createTable2(estimation));

            doc.close();

        } catch (DocumentException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private PdfPTable crateTable1(Estimation estimation) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4f,7f, 4f, 3f});

        table.addCell(createLabelCell("Przedmiot:"));
        table.addCell(createValue2Cell(estimation.getItemName()));

        table.addCell(createLabelCell("Ilość:"));
        table.addCell(createValue2Cell(String.valueOf(estimation.getAmount())+" SZT"));

        table.addCell(createLabelCell("Materiał:"));

        StringBuilder materialSb = new StringBuilder();
        if(estimation.getMaterial()!=null){
            materialSb.append(estimation.getMaterial());
        }
        if(estimation.getMaterialType()!=null){
            materialSb.append(", gatunek: ").append(estimation.getMaterialType());
        }
        table.addCell(createValue2Cell(materialSb.toString()));

        table.addCell(createLabelCell(""));
        table.addCell(createValue2Cell(""));

        table.addCell(createLabelCell("Termin realizacji:"));
        if (estimation.getEstimatedRealizationDate() != null) {
            table.addCell(createValue2Cell(formatDate(estimation.getEstimatedRealizationDate())));
        } else {
            table.addCell(createValue2Cell(""));
        }

        table.addCell(createLabelCell("Wydano do prod.:"));
        table.addCell(createValue2Cell(formatDate(LocalDate.now())));


        return table;
    }

    // create cells

    private PdfPTable createTable2(Estimation estimation) throws DocumentException, IOException {

        // create 6 column table
        PdfPTable table = new PdfPTable(4);

        // set the width of the table to 100% of page
        table.setWidthPercentage(100);

        // set relative columns width
        table.setWidths(new float[]{1.5f, 11f, 2.5f, 2f});



        addHeader(table, "LP", "Stanowisko", "Kod", "Opis", "Czas wykonania w godz.", "Podpis");

        int i = 1;
        estimation.getOperations().sort(Comparator.comparing(Operation::getSequenceNumber));
        for (Operation operation : estimation.getOperations()) {
            Image image = barcodeImage(operation.getId());

            addRowTable2(table, String.valueOf(operation.getSequenceNumber()), operation.getMachine().getName(), image, operation.getDescription(),
                         String.valueOf(operation.getId()));
            i++;
        }


        return table;


    }

    private void addRowTable2(PdfPTable table, String lp, String machine, Image image, String description, String operationNumber) throws DocumentException {
        PdfPTable nestedTable = new PdfPTable(2);

        PdfPTable barcodeTable = createBarcodeTable(image, operationNumber);

        nestedTable.setWidths(new float[]{2f, 1f});
        nestedTable.addCell(createNestedTableCellWithoutBorder(machine));

        PdfPCell barcodeCell = new PdfPCell(barcodeTable);
        barcodeCell.setBorder(0);
        nestedTable.addCell(barcodeCell);
/*        nestedTable.addCell("");
        nestedTable.addCell("1234");*/
        nestedTable.addCell(createColspannedCell(description));

        //    nestedTable.addCell(createLabel2Cell("Kod"));

        PdfPCell cellWithNested = new PdfPCell(nestedTable);
        //-----------------Table Cells Label/Value------------------

        // 1st Row
        table.addCell(createNestedTableHeaderCell(lp));
        table.addCell(cellWithNested);
        table.addCell(createLabel2Cell(""));
        table.addCell(createLabel2Cell(""));
    }

    private PdfPTable createBarcodeTable(Image image, String operationNumber) {
        PdfPTable barcodeTable = new PdfPTable(1);
        barcodeTable.getDefaultCell().setBorder(0);
        PdfPCell barcodeNumberCell = new PdfPCell(new Phrase(operationNumber, new Font(baseFont, 8)));
        barcodeNumberCell.setVerticalAlignment(Element.ALIGN_CENTER);
        barcodeNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        barcodeNumberCell.setBorder(0);
        barcodeNumberCell.setPaddingTop(4f);
        barcodeNumberCell.setPaddingBottom(1f);
        barcodeTable.addCell(barcodeNumberCell);
        barcodeTable.addCell(createImageCell(image));
        return barcodeTable;
    }

    private void addHeader(PdfPTable table, String lp, String machine, String barcode, String description, String time, String createdBy) throws DocumentException {
        PdfPTable nestedTable = new PdfPTable(2);
        nestedTable.setWidths(new float[]{2f, 1f});
        nestedTable.addCell(createNestedTableHeaderCell(machine));
        nestedTable.addCell(createNestedTableHeaderCell(barcode));

        nestedTable.addCell(createColspannedCell(description));

        //    nestedTable.addCell(createLabel2Cell("Kod"));

        PdfPCell cellWithNested = new PdfPCell(nestedTable);
        //-----------------Table Cells Label/Value------------------

        // 1st Row
        table.addCell(createNestedTableHeaderCell(lp));
        table.addCell(cellWithNested);
        table.addCell(createNestedTableHeaderCell(time));
        table.addCell(createNestedTableHeaderCell(createdBy));
    }


    private Image barcodeImage(Long operationId) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            barcodeCreatorService.createBarcode(String.valueOf(operationId), byteArrayOutputStream);
            Image image= Image.getInstance(byteArrayOutputStream.toByteArray());
            return image;

        } catch (BadElementException | IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

}
