package uk.gov.hscic.order.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.order.model.OrderDetail;
import uk.gov.hscic.order.model.OrderEntity;
import uk.gov.hscic.order.repo.OrderRepository;
import uk.gov.hscic.order.search.OrderEntityToOrderDetailTransformer;

@Service
public class OrderStore {
    private final OrderEntityToOrderDetailTransformer entityToDetailTransformer = new OrderEntityToOrderDetailTransformer();
    private final OrderDetailToOrderEntityTransformer detailToEntityTransformer = new OrderDetailToOrderEntityTransformer();

    @Autowired
    private OrderRepository orderRepository;

    public OrderDetail saveOrder(OrderDetail orderDetail) {
        OrderEntity orderEntity = detailToEntityTransformer.transform(orderDetail);
        orderEntity = orderRepository.saveAndFlush(orderEntity);
        return entityToDetailTransformer.transform(orderEntity);
    }

    public void clearOrders() {
        orderRepository.deleteAll();
    }
}
