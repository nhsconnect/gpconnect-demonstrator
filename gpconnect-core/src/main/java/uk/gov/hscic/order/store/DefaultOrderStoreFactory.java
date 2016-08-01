package uk.gov.hscic.order.store;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultOrderStoreFactory  extends AbstractRepositoryFactory<OrderStore> implements OrderStoreFactory {

    @Override
    protected OrderStore defaultRepository() {
        return new NotConfiguredOrderStore();
    }

    @Override
    protected Class<OrderStore> repositoryClass() {
        return OrderStore.class;
    }
}