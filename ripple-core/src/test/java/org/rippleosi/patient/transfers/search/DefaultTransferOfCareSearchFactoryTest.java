package org.rippleosi.patient.transfers.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTransferOfCareSearchFactoryTest
    extends AbstractRepositoryFactoryTest<TransferOfCareSearchFactory, TransferOfCareSearch> {

    @Override
    protected TransferOfCareSearchFactory createRepositoryFactory() {
        return new DefaultTransferOfCareSearchFactory();
    }

    @Override
    protected Class<TransferOfCareSearch> getRepositoryClass() {
        return TransferOfCareSearch.class;
    }
}
