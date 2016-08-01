package uk.gov.hscic.order.store;

import uk.gov.hscic.common.repo.RepositoryFactory;

@FunctionalInterface
public interface OrderStoreFactory extends RepositoryFactory<OrderStore> {

}
