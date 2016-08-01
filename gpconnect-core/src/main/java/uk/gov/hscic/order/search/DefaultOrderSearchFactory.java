package uk.gov.hscic.order.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultOrderSearchFactory extends AbstractRepositoryFactory<OrderSearch> implements OrderSearchFactory {

    @Override
    protected OrderSearch defaultRepository() {
        return new NotConfiguredOrderSearch();
    }

    @Override
    protected Class<OrderSearch> repositoryClass() {
        return OrderSearch.class;
    }
}