package org.rippleosi.patient.transfers.search;

import org.rippleosi.common.search.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultTransferOfCareSearchFactory extends AbstractRepositoryFactory<TransferOfCareSearch>
    implements TransferOfCareSearchFactory {

    @Override
    protected TransferOfCareSearch defaultRepository() {
        return new NotConfiguredTransferOfCareSearch();
    }

    @Override
    protected Class<TransferOfCareSearch> repositoryClass() {
        return TransferOfCareSearch.class;
    }
}
