package eu.canpack.fip.bo.production;

import eu.canpack.fip.bo.order.dto.OrderListDTO;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import eu.canpack.fip.bo.referenceOrder.ReferenceOrder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * CP S.A.
 * Created by lukasz.mochel on 15.12.2017.
 */
@Service
public class ProductionExcelService {

    public void createExcelFile(List<ProductionItemDTO> orders, OutputStream os) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        //Create blank workbook
        Workbook workbook = new XSSFWorkbook();

        //Create a blank sheet

        Sheet spreadsheet = workbook.createSheet("Data");


        createHeader(spreadsheet);
        int rowId = 1;


        for (ProductionItemDTO itemDTO : orders) {
            int columnNo = 0;
            Row row = spreadsheet.createRow(rowId);
            row.createCell(columnNo++).setCellValue(itemDTO.getEstimationId());
            row.createCell(columnNo++).setCellValue(itemDTO.getClientName());
            row.createCell(columnNo++).setCellValue(itemDTO.getItemNumber());
            row.createCell(columnNo++).setCellValue(itemDTO.getItemName());
            row.createCell(columnNo++).setCellValue(itemDTO.getAmount());
            row.createCell(columnNo++).setCellValue(itemDTO.getOrderNumber());
            if (itemDTO.getProductionStartDateTime() != null) {
                row.createCell(columnNo++).setCellValue(itemDTO.getProductionStartDateTime().format(formatter));

            } else {
                row.createCell(columnNo++).setCellValue("");
            }
            if (itemDTO.getEstimatedRealizationDate() != null) {
                row.createCell(columnNo++).setCellValue(itemDTO.getEstimatedRealizationDate().format(dateFormatter));

            } else {
                row.createCell(columnNo++).setCellValue("");
            }

            row.createCell(columnNo++).setCellValue(itemDTO.getProductionProgress());
            row.createCell(columnNo++).setCellValue(itemDTO.getNextOperationPlace());
            row.createCell(columnNo++).setCellValue(itemDTO.getMpk());

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
        cell1.setCellValue("Klient");

        Cell cell2 = headerRow.createCell(columnNo++);
        cell2.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell2.setCellValue("Nr rys");

        Cell cell3 = headerRow.createCell(columnNo++);
        cell3.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell3.setCellValue("Nazwa");

        Cell cell4 = headerRow.createCell(columnNo++);
        cell4.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell4.setCellValue("Ilość [szt]");

        Cell cell5 = headerRow.createCell(columnNo++);
        cell5.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell5.setCellValue("Zlecenie");

        Cell cell6 = headerRow.createCell(columnNo++);
        cell6.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell6.setCellValue("Data przekazania do prod");


        Cell cell8 = headerRow.createCell(columnNo++);
        cell8.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell8.setCellValue("Planowana data realiz.");

        Cell cell9 = headerRow.createCell(columnNo++);
        cell9.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell9.setCellValue("Ukończono [%]");

        Cell cell10 = headerRow.createCell(columnNo++);
        cell10.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell10.setCellValue("Stanowisko aktualnej operacji");

        Cell cell11 = headerRow.createCell(columnNo++);
        cell11.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell11.setCellValue("MPK/Index");


    }


    CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;

    }


}
