package uk.gov.hscic.order.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.order.model.OrderDetail;
import uk.gov.hscic.order.model.OrderEntity;
import uk.gov.hscic.order.repo.OrderRepository;

@Service
public class LegacyOrderSearch extends AbstractLegacyService implements OrderSearch {

    @Autowired
    private OrderRepository orderRepository;

    private final OrderEntityToOrderDetailTransformer transformer = new OrderEntityToOrderDetailTransformer();

    @Override
    public OrderDetail findOrderByID(Long id) {
        final OrderEntity item = orderRepository.findOne(id);
        if(item == null){
            return null;
        } else {
            return transformer.transform(item);
        }
    }

    @Override
    public List<OrderDetail> findOrdersForPatientId(Long patinetId) {
        List<OrderEntity> items = orderRepository.findBySubjectPatientId(patinetId);
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for(OrderEntity entity : items){
            orderDetails.add(transformer.transform(entity));
        }
        return orderDetails;
    }

}
