package eu.canpack.fip.bo.order

import eu.canpack.fip.bo.attachment.AttachmentRepository
import eu.canpack.fip.bo.client.ClientRepository
import eu.canpack.fip.bo.drawing.DrawingRepository
import eu.canpack.fip.bo.estimation.EstimationRepository
import eu.canpack.fip.bo.mpkBudgetMapper.MpkBudgetMapperService
import eu.canpack.fip.bo.order.dto.OrderMapperImpl
import eu.canpack.fip.bo.referenceOrder.ReferenceOrderRepository
import eu.canpack.fip.config.ApplicationProperties
import eu.canpack.fip.repository.UserRepository
import eu.canpack.fip.repository.search.OrderSearchRepository
import eu.canpack.fip.service.UserService
import groovy.util.logging.Slf4j
import spock.lang.Specification

/**
 * CP S.A.
 * Created by lukasz.mochel on 28.01.2018.
 */
@Slf4j
class OrderServiceTest extends Specification {
    private OrderService orderService
    private OrderRepository orderRepository=Mock();
    private OrderSearchRepository orderSearchRepository=Mock()
    private UserRepository userRepository=Mock()
    private UserService userService=Mock()
    private DrawingRepository drawingRepository=Mock()
    private AttachmentRepository attachmentRepository=Mock()
    private EstimationRepository estimationRepository=Mock()
    private ApplicationProperties applicationProperties=Mock()
    private ClientRepository clientRepository=Mock()
    private ReferenceOrderRepository referenceOrderRepository=Mock()
    private MpkBudgetMapperService mapperService;


    void setup() {
        orderService=new OrderService(orderRepository,new OrderMapperImpl(),orderSearchRepository,userRepository,
        drawingRepository,attachmentRepository,estimationRepository,userService,applicationProperties,clientRepository,
        referenceOrderRepository,mapperService)
    }

    void cleanup() {
    }

    def "CopyAttachmentFile"() {
        log.debug("test")
        when:
        orderService.copyAttachmentFile("D:\\uploaded_from_tn\\dr_2017-12-03_01.46.53.pdf")

        then:
        true
    }
}
