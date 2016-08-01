package uk.gov.hscic.order.store;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.order.model.OrderDetail;

public interface OrderStore extends Repository {

    OrderDetail saveOrder(OrderDetail orderDetail);
    
}
