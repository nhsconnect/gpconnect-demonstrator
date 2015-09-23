package org.rippleosi.patient.terminology.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTerminologySearchFactoryTest
    extends AbstractRepositoryFactoryTest<TerminologySearchFactory, TerminologySearch> {

    @Override
    protected TerminologySearchFactory createRepositoryFactory() {
        return new DefaultTerminologySearchFactory();
    }

    @Override
    protected Class<TerminologySearch> getRepositoryClass() {
        return TerminologySearch.class;
    }
}
