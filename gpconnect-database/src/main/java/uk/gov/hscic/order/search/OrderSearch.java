package uk.gov.hscic.order.search;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.order.model.OrderDetail;
import uk.gov.hscic.order.model.OrderEntity;
import uk.gov.hscic.order.repo.OrderRepository;

@Service
public class OrderSearch {
    private final OrderEntityToOrderDetailTransformer transformer = new OrderEntityToOrderDetailTransformer();

    @Autowired
    private OrderRepository orderRepository;

    public OrderDetail findOrderByID(Long id) {
        final OrderEntity item = orderRepository.findById(id).get();

        return item == null
                ? null
                : transformer.transform(item);
    }

    public List<OrderDetail> findOrdersForPatientId(Long patinetId) {
        return orderRepository.findBySubjectPatientId(patinetId)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
    }
}
