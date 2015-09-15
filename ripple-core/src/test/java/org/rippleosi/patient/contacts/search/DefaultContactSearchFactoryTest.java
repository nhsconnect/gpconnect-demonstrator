package org.rippleosi.patient.contacts.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.search.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultContactSearchFactoryTest extends AbstractRepositoryFactoryTest<ContactSearchFactory,ContactSearch> {

    @Override
    protected ContactSearchFactory createSearchFactory() {
        return new DefaultContactSearchFactory();
    }

    @Override
    protected Class<ContactSearch> getSearchClass() {
        return ContactSearch.class;
    }
}
