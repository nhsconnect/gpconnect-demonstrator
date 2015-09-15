package org.rippleosi.patient.allergies.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.search.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAllergySearchFactoryTest extends AbstractRepositoryFactoryTest<AllergySearchFactory,AllergySearch> {

    @Override
    protected AllergySearchFactory createSearchFactory() {
        return new DefaultAllergySearchFactory();
    }

    @Override
    protected Class<AllergySearch> getSearchClass() {
        return AllergySearch.class;
    }
}
