package org.rippleosi.patient.allergies.store;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.search.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAllergyStoreFactoryTest extends AbstractRepositoryFactoryTest<AllergyStoreFactory, AllergyStore> {

    @Override
    protected AllergyStoreFactory createRepositoryFactory() {
        return new DefaultAllergyStoreFactory();
    }

    @Override
    protected Class<AllergyStore> getRepositoryClass() {
        return AllergyStore.class;
    }
}
