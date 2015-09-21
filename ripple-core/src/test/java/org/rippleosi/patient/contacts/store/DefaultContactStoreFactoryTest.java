package org.rippleosi.patient.contacts.store;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultContactStoreFactoryTest extends AbstractRepositoryFactoryTest<ContactStoreFactory,ContactStore> {

    @Override
    protected ContactStoreFactory createRepositoryFactory() {
        return new DefaultContactStoreFactory();
    }

    @Override
    protected Class<ContactStore> getRepositoryClass() {
        return ContactStore.class;
    }
}
