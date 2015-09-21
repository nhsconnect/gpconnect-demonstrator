package org.rippleosi.patient.allergies.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAllergySearchFactoryTest extends AbstractRepositoryFactoryTest<AllergySearchFactory,AllergySearch> {

    @Override
    protected AllergySearchFactory createRepositoryFactory() {
        return new DefaultAllergySearchFactory();
    }

    @Override
    protected Class<AllergySearch> getRepositoryClass() {
        return AllergySearch.class;
    }
}
