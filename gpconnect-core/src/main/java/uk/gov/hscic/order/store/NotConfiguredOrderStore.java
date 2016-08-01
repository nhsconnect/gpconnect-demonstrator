package uk.gov.hscic.order.store;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.order.model.OrderDetail;

public class NotConfiguredOrderStore implements OrderStore {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public OrderDetail saveOrder(OrderDetail orderDetail) {
        throw ConfigurationException.unimplementedTransaction(OrderStore.class);
    }


}
