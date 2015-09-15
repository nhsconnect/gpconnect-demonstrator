package org.rippleosi.patient.contacts.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.search.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultContactSearchFactoryTest extends AbstractRepositoryFactoryTest<ContactSearchFactory,ContactSearch> {

    @Override
    protected ContactSearchFactory createRepositoryFactory() {
        return new DefaultContactSearchFactory();
    }

    @Override
    protected Class<ContactSearch> getRepositoryClass() {
        return ContactSearch.class;
    }
}
