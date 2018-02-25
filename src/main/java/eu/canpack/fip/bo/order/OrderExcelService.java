package eu.canpack.fip.bo.order;

import eu.canpack.fip.bo.order.dto.OrderListDTO;
import eu.canpack.fip.bo.order.enumeration.OrderType;
import eu.canpack.fip.bo.referenceOrder.ReferenceOrder;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.awt.Color;
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
public class OrderExcelService {

    public void createExcelFile(List<OrderListDTO> orders, OrderType orderType, OutputStream os) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        //Create blank workbook
        Workbook workbook = new XSSFWorkbook();

        //Create a blank sheet

        Sheet spreadsheet = workbook.createSheet("Data");


        createHeader(spreadsheet,orderType);
        int rowId = 1;


        for (OrderListDTO o : orders) {
            int columnNo=0;
            Row row = spreadsheet.createRow(rowId);
            row.createCell(columnNo++).setCellValue(o.getId());
            row.createCell(columnNo++).setCellValue(o.getInternalNumber());
            row.createCell(columnNo++).setCellValue(o.getReferenceNumber());
            row.createCell(columnNo++).setCellValue(o.getName());
            row.createCell(columnNo++).setCellValue(o.getCreatedBy().getFirstName() + " " + o.getCreatedBy().getLastName());
            row.createCell(columnNo++).setCellValue(o.getCreatedAt().format(formatter));
            row.createCell(columnNo++).setCellValue(o.getOrderStatus().getPlName());
            if(orderType!=OrderType.EMERGENCY) {
                row.createCell(columnNo++).setCellValue(o.getReferenceOrders().stream()
                                                            .map(ReferenceOrder::getRefInternalNumber)
                                                            .collect(Collectors.joining(", ")));
            }
            row.createCell(columnNo++).setCellValue(o.getClientShortcut());

            if(o.getEstimationMaker()!=null){
                row.createCell(columnNo++).setCellValue(o.getEstimationMaker().getFirstName()+" "+o.getEstimationMaker().getLastName());

            }else{
                row.createCell(columnNo++).setCellValue("");
            }

            rowId++;
        }

        IntStream.range(0, 9).forEach(spreadsheet::autoSizeColumn);
        spreadsheet.createFreezePane(0, 1);

        workbook.write(os);

    }

    private void createHeader(Sheet spreadsheet, OrderType orderType) {

        Row headerRow = spreadsheet.createRow(0);

        int columnNo=0;
        Cell cell0 = headerRow.createCell(columnNo++);
        cell0.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell0.setCellValue("Id");

        Cell cell1 = headerRow.createCell(columnNo++);
        cell1.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell1.setCellValue("Nr wewnętrzny");

        Cell cell2 = headerRow.createCell(columnNo++);
        cell2.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell2.setCellValue("Nr klienta");

        Cell cell3 = headerRow.createCell(columnNo++);
        cell3.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell3.setCellValue("Tytuł");

        Cell cell4 = headerRow.createCell(columnNo++);
        cell4.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell4.setCellValue("Utworzył");

        Cell cell5 = headerRow.createCell(columnNo++);
        cell5.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell5.setCellValue("Data utworzenia");

        Cell cell6 = headerRow.createCell(columnNo++);
        cell6.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell6.setCellValue("Status");


        if(orderType!=OrderType.EMERGENCY){
        Cell cell7 = headerRow.createCell(columnNo++);
        cell7.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        switch (orderType) {
            case ESTIMATION:
                cell7.setCellValue("Zamowienia");
                break;
            case PRODUCTION:
                cell7.setCellValue("Do oferty");
                break;
            default:
                cell7.setCellValue("");
                break;

        }}

        Cell cell8 = headerRow.createCell(columnNo++);
        cell8.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell8.setCellValue("Klient");


        Cell cell9 = headerRow.createCell(columnNo);
        cell9.setCellStyle(getHeaderCellStyle(spreadsheet.getWorkbook()));
        cell9.setCellValue("Wycenę/Technologię utworzył");


    }


    CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;

    }


}
