package org.rippleosi.patient.laborders.store;

import org.rippleosi.common.search.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultLabOrderStoreFactory extends AbstractRepositoryFactory<LabOrderStore> implements LabOrderStoreFactory {

    @Override
    protected LabOrderStore defaultRepository() {
        return new NotConfiguredLabOrderStore();
    }

    @Override
    protected Class<LabOrderStore> repositoryClass() {
        return LabOrderStore.class;
    }
}
