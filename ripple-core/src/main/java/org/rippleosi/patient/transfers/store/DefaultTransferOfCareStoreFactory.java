package org.rippleosi.patient.transfers.store;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultTransferOfCareStoreFactory extends AbstractRepositoryFactory<TransferOfCareStore>
    implements TransferOfCareStoreFactory {

    @Override
    protected TransferOfCareStore defaultRepository() {
        return new NotConfiguredTransferOfCareStore();
    }

    @Override
    protected Class<TransferOfCareStore> repositoryClass() {
        return TransferOfCareStore.class;
    }
}
