package org.rippleosi.patient.transfers.store;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTransferOfCareStoreFactoryTest
    extends AbstractRepositoryFactoryTest<TransferOfCareStoreFactory, TransferOfCareStore> {

    @Override
    protected TransferOfCareStoreFactory createRepositoryFactory() {
        return new DefaultTransferOfCareStoreFactory();
    }

    @Override
    protected Class<TransferOfCareStore> getRepositoryClass() {
        return TransferOfCareStore.class;
    }
}
