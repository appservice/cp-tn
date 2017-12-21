package eu.canpack.fip.bo.order;

import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeWithZoneIdSerializer;
import eu.canpack.fip.bo.order.dto.OrderDTO;
import eu.canpack.fip.bo.order.dto.OrderListDTO;
import eu.canpack.fip.bo.order.enumeration.OrderStatus;
import eu.canpack.fip.service.dto.UserShortDTO;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * CP S.A.
 * Created by lukasz.mochel on 16.12.2017.
 */
public class OrderExcelServiceTest {

    private OrderExcelService orderExcelService;

    @Before
    public void setUp() throws Exception {
        orderExcelService=new OrderExcelService();


    }

    @Test
    public void createFile() throws IOException {
        OutputStream fos = new FileOutputStream("tst.xlsx");
       // orderExcelService.createExcelFile(createOrderDtoList(), fos);
        fos.close();
    }

    public List<OrderListDTO> createOrderDtoList(){
        List<OrderListDTO> orderDTOS = new ArrayList<>();
        OrderListDTO o1 = new OrderListDTO();
        o1.setId(12L);
        o1.setInternalNumber("int/numb/test");
        o1.setReferenceNumber("fer/number/2017");
        o1.setName("testowe zamóweieni");
        o1.setOrderStatus(OrderStatus.WORKING_COPY);
        o1.setClientShortcut("shortcut client");
//        UserShortDTO userShortDTO
//        o1.setCreatedBy();
       // o1.setCreatedByName(" Józek mędrek");
        o1.setCreatedAt(ZonedDateTime.of(2017, 12, 12, 12, 0, 0, 0, ZoneId.of("UTC")));
        orderDTOS.add(o1);
        return orderDTOS;
    }

}
