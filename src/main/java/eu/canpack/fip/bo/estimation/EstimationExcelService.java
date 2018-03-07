package eu.canpack.fip.bo.estimation;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
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


            if(o.getEstimatedCost()!=null){
                row.createCell(columnNo++).setCellValue(o.getEstimatedCost().toString());

            }else{
                row.createCell(columnNo++).setCellValue("");
            }


            row.createCell(columnNo++).setCellValue(o.getOrder().getInternalNumber());
            row.createCell(columnNo++).setCellValue(o.getOrder().getOrderType().getPlName());






            rowId++;
        }

        IntStream.range(0, 8).forEach(spreadsheet::autoSizeColumn);
        spreadsheet.createFreezePane(0, 1);

        workbook.write(os);

    }

    private void createHeader(Sheet spreadsheet) {

        Row headerRow = spreadsheet.createRow(0);

        int columnNo = 0;
        Cell cell0 = headerRow.createCell(columnNo++);
        cell0.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell0.setCellValue("Id");

        Cell cell1 = headerRow.createCell(columnNo++);
        cell1.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell1.setCellValue("Nazwa");

        Cell cell2 = headerRow.createCell(columnNo++);
        cell2.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell2.setCellValue("Nr rys.");

        Cell cell3 = headerRow.createCell(columnNo++);
        cell3.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell3.setCellValue("Ilość");

        Cell cell4 = headerRow.createCell(columnNo++);
        cell4.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell4.setCellValue("Utworzył");

        Cell cell5 = headerRow.createCell(columnNo++);
        cell5.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell5.setCellValue("Data utworz");

        Cell cell6 = headerRow.createCell(columnNo++);
        cell6.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell6.setCellValue("Cena [PLN]/szt");


        Cell cell8 = headerRow.createCell(columnNo++);
        cell8.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell8.setCellValue("Nr zlecenia");
//
//
        Cell cell9 = headerRow.createCell(columnNo);
        cell9.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell9.setCellValue("Typ zlecenia");


    }


    CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;

    }


}
