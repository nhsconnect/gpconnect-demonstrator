package uk.gov.hscic.order.search;

import java.util.List;
import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.order.model.OrderDetail;

public interface OrderSearch extends Repository {
    
    OrderDetail findOrderByID(Long id);
    List<OrderDetail> findOrdersForPatientId(Long patientId);
}
