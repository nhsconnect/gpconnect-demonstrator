package org.rippleosi.patient.labresults.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.search.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultLabResultSearchFactoryTest extends AbstractRepositoryFactoryTest<LabResultSearchFactory, LabResultSearch> {

    @Override
    protected LabResultSearchFactory createRepositoryFactory() {
        return new DefaultLabResultSearchFactory();
    }

    @Override
    protected Class<LabResultSearch> getRepositoryClass() {
        return LabResultSearch.class;
    }
}
