package eu.canpack.fip.bo.order

import eu.canpack.fip.TnApp
import eu.canpack.fip.bo.attachment.AttachmentRepository
import eu.canpack.fip.bo.drawing.DrawingRepository
import eu.canpack.fip.bo.estimation.EstimationRepository
import eu.canpack.fip.repository.UserRepository
import eu.canpack.fip.repository.search.OrderSearchRepository
import eu.canpack.fip.service.UserService
import groovy.util.logging.Log
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

/**
 * CP S.A.
 * Created by lukasz.mochel on 03.11.2017.
 */
@SpringBootTest(classes = [TnApp.class])
@ActiveProfiles(["test"])
@Slf4j
class OrderServiceTest extends Specification {

    //@Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;

    OrderRepository orderRepository = Mock(OrderRepository)


    void setup() {
        OrderSearchRepository orderSearchRepository = Mock(OrderSearchRepository)
        UserRepository userRepository = Mock(UserRepository)
        DrawingRepository drawingRepository = Mock(DrawingRepository)
        AttachmentRepository attachmentRepository = Mock(AttachmentRepository)
        EstimationRepository estimationRepository = Mock(EstimationRepository)
        UserService userService = Mock(UserService)


        orderService = new OrderService(orderRepository, orderMapper, orderSearchRepository, userRepository, drawingRepository, attachmentRepository, estimationRepository, userService)
    }

    @Unroll
    def "FindOne #id"() {


        given:
        Order order = new Order();
        order.setId(1l)

        when:
        Order o = orderService.findOne(id)
        log.info("order: {}", o)
        then:
        1 * orderRepository.findOne(_)>>order
        log.info("order: {}", o)

        true;
        o == response

        where:
        id || response
        1L || _
        2L || _

    }
}
