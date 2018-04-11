package eu.canpack.fip.service;

import com.codahale.metrics.annotation.Timed;
import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.client.ClientRepository;
import eu.canpack.fip.bo.commercialPart.CommercialPart;
import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.drawing.DrawingRepository;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.estimation.EstimationRepository;
import eu.canpack.fip.bo.machine.Machine;
import eu.canpack.fip.bo.machine.MachineRepository;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.operation.OperationRepository;
import eu.canpack.fip.bo.operator.OperatorRepository;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.order.OrderRepository;
import eu.canpack.fip.bo.technologyCard.TechnologyCardRepository;
import eu.canpack.fip.domain.Unit;
import eu.canpack.fip.domain.User;
import eu.canpack.fip.repository.CommercialPartRepository;
import eu.canpack.fip.repository.UnitRepository;
import eu.canpack.fip.repository.UserRepository;
import eu.canpack.fip.repository.search.*;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.Operator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    private final ClientRepository clientRepository;

    private final ClientSearchRepository clientSearchRepository;

//    private final CommercialPartRepository commercialPartRepository;
//
//    private final CommercialPartSearchRepository commercialPartSearchRepository;
//
//    private final DrawingRepository drawingRepository;
//
//    private final DrawingSearchRepository drawingSearchRepository;
//
//    private final EstimationRepository estimationRepository;
//
//    private final EstimationSearchRepository estimationSearchRepository;
//
//    private final MachineRepository machineRepository;
//
//    private final MachineSearchRepository machineSearchRepository;
//
//    private final OperationRepository operationRepository;
//
//    private final OperationSearchRepository operationSearchRepository;
//
//    private final OrderRepository orderRepository;
//
//    private final OrderSearchRepository orderSearchRepository;

    private final UnitRepository unitRepository;

    private final UnitSearchRepository unitSearchRepository;

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

//    private final TechnologyCardRepository technologyCardRepository;

    private final OperatorSearchRepository operatorSearchRepository;

    private final OperatorRepository operatorRepository;

//    private final TechnologyCardSearchRepository technologyCardSearchRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ElasticsearchIndexService(
        UserRepository userRepository,
        UserSearchRepository userSearchRepository,
        ClientRepository clientRepository,
        ClientSearchRepository clientSearchRepository,
//        CommercialPartRepository commercialPartRepository,
//        CommercialPartSearchRepository commercialPartSearchRepository,
//        DrawingRepository drawingRepository,
//        DrawingSearchRepository drawingSearchRepository,
//        EstimationRepository estimationRepository,
//        EstimationSearchRepository estimationSearchRepository,
//        MachineRepository machineRepository,
//        MachineSearchRepository machineSearchRepository,
//        OperationRepository operationRepository,
//        OperationSearchRepository operationSearchRepository,
//        OrderRepository orderRepository,
//        OrderSearchRepository orderSearchRepository,
        UnitRepository unitRepository,
        UnitSearchRepository unitSearchRepository,
        TechnologyCardRepository technologyCardRepository, /*TechnologyCardSearchRepository technologyCardSearchRepository,*/ OperatorSearchRepository operatorSearchRepository, OperatorRepository operatorRepository, ElasticsearchTemplate elasticsearchTemplate) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.clientRepository = clientRepository;
        this.clientSearchRepository = clientSearchRepository;
//        this.commercialPartRepository = commercialPartRepository;
//        this.commercialPartSearchRepository = commercialPartSearchRepository;
//        this.drawingRepository = drawingRepository;
//        this.drawingSearchRepository = drawingSearchRepository;
//        this.estimationRepository = estimationRepository;
//        this.estimationSearchRepository = estimationSearchRepository;
//        this.machineRepository = machineRepository;
//        this.machineSearchRepository = machineSearchRepository;
//        this.operationRepository = operationRepository;
//        this.operationSearchRepository = operationSearchRepository;
//        this.orderRepository = orderRepository;
//        this.orderSearchRepository = orderSearchRepository;
        this.unitRepository = unitRepository;
        this.unitSearchRepository = unitSearchRepository;
        this.operatorSearchRepository = operatorSearchRepository;
        this.operatorRepository = operatorRepository;
//        this.technologyCardRepository = technologyCardRepository;
//        this.technologyCardSearchRepository = technologyCardSearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Async
    @Timed
    public void reindexAll() {
        reindexForClass(Client.class, clientRepository, clientSearchRepository);
//        reindexForClass(CommercialPart.class, commercialPartRepository, commercialPartSearchRepository);
//        reindexForClass(Drawing.class, drawingRepository, drawingSearchRepository);
//        reindexForClass(Estimation.class, estimationRepository, estimationSearchRepository);
//        reindexForClass(Machine.class, machineRepository, machineSearchRepository);
//        reindexForClass(Operation.class, operationRepository, operationSearchRepository);
//        reindexForClass(Order.class, orderRepository, orderSearchRepository);
        reindexForClass(Unit.class, unitRepository, unitSearchRepository);
        reindexForClass(User.class, userRepository, userSearchRepository);
        reindexForClass(eu.canpack.fip.bo.operator.Operator.class, operatorRepository, operatorSearchRepository);
//        reindexForClass(TechnologyCard.class,technologyCardRepository,technologyCardSearchRepository);

        log.info("Elasticsearch: Successfully performed reindexing");
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
                                                             ElasticsearchRepository<T, ID> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            try {
                Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
                elasticsearchRepository.save((List<T>) m.invoke(jpaRepository));
            } catch (Exception e) {
                elasticsearchRepository.save(jpaRepository.findAll());
            }
        }
        log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
    }
}
