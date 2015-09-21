package org.rippleosi.patient.laborders.store;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultLabOrderStoreFactoryTest extends AbstractRepositoryFactoryTest<LabOrderStoreFactory, LabOrderStore> {

    @Override
    protected LabOrderStoreFactory createRepositoryFactory() {
        return new DefaultLabOrderStoreFactory();
    }

    @Override
    protected Class<LabOrderStore> getRepositoryClass() {
        return LabOrderStore.class;
    }
}
