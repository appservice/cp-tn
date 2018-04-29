package eu.canpack.fip.bo.estimation;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * CP S.A.
 * Created by lukasz.mochel on 15.12.2017.
 */
@Service
@Transactional
public class EstimationExcelService {

    public void createExcelFile(List<Estimation> estimations, OutputStream os) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        //Create blank workbook
        Workbook workbook = new XSSFWorkbook();

        //Create a blank sheet

        Sheet spreadsheet = workbook.createSheet("Data");


        createHeader(spreadsheet);
        int rowId = 1;


        for (Estimation o : estimations) {
            int columnNo = 0;
            Row row = spreadsheet.createRow(rowId);
            row.createCell(columnNo++).setCellValue(o.getId());
            row.createCell(columnNo++).setCellValue(o.getItemName());

            String number = Optional.ofNullable(o.getItemNumber()).orElse("");
            row.createCell(columnNo++).setCellValue(number);

            row.createCell(columnNo++).setCellValue(o.getAmount());

            if (o.getCreatedBy() != null) {
                row.createCell(columnNo++).setCellValue(o.getCreatedBy().getFirstName() + " " + o.getCreatedBy().getLastName());
                row.createCell(columnNo++).setCellValue(o.getCreatedAt().format(formatter));

            } else {
                row.createCell(columnNo++).setCellValue("");
                row.createCell(columnNo++).setCellValue("");

            }


            if (o.getEstimatedCost() != null) {
                row.createCell(columnNo++).setCellValue(o.getEstimatedCost().toString().replace(".", ","));


            } else {
                row.createCell(columnNo++).setCellValue("");
            }


            row.createCell(columnNo++).setCellValue(o.getOrder().getInternalNumber());
            row.createCell(columnNo++).setCellValue(o.getOrder().getOrderType().getPlName());
            row.createCell(columnNo++).setCellValue(o.getOrder().getClient().getShortcut());
            row.createCell(columnNo++).setCellValue(o.getMpk());
            if (o.getOrder().getCreatedAt() != null) {
                row.createCell(columnNo++).setCellValue(o.getOrder().getCreatedAt().format(formatter));

            } else {
                row.createCell(columnNo++).setCellValue("");
            }

            row.createCell(columnNo++).setCellValue(o.getOrder().getCreatedBy().getFirstName() + " " + o.getOrder().getCreatedBy().getLastName());


            rowId++;
        }

        IntStream.range(0, 8).forEach(spreadsheet::autoSizeColumn);
        spreadsheet.createFreezePane(0, 1);

        workbook.write(os);

    }

    private void createHeader(Sheet spreadsheet) {

        Row headerRow = spreadsheet.createRow(0);

        int columnNo = 0;

        createHeaderCell(spreadsheet, headerRow, columnNo++, "Id");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "Nazwa");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "Nr rys.");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "Ilość");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "Wycene utworzył");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "Data utworz. wyceny/karty techn.");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "Cena [PLN]/szt");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "Nr zlecenia");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "Typ zlecenia");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "Klient");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "MPK/Index");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "Data zapytania/zamówienia");
        createHeaderCell(spreadsheet, headerRow, columnNo++, "Zapytanie/zamówienie utworzył");


    }

    private void createHeaderCell(Sheet spreadsheet, Row headerRow, int columnNo, String cellValue) {
        Cell cell13 = headerRow.createCell(columnNo);
        cell13.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell13.setCellValue(cellValue);
    }

    CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;

    }


    private String getFormattedPrice(BigDecimal price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format(price);
    }
}
