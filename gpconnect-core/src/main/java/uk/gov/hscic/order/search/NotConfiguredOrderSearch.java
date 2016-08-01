package uk.gov.hscic.order.search;

import java.util.List;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.order.model.OrderDetail;

public class NotConfiguredOrderSearch implements OrderSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public OrderDetail findOrderByID(Long id) {
        throw ConfigurationException.unimplementedTransaction(OrderSearch.class);
    }

    @Override
    public List<OrderDetail> findOrdersForPatientId(Long patientId) {
        throw ConfigurationException.unimplementedTransaction(OrderSearch.class);
    }

}
