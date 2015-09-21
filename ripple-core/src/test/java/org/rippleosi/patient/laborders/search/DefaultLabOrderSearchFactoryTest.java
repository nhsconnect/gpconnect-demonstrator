package org.rippleosi.patient.laborders.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultLabOrderSearchFactoryTest extends AbstractRepositoryFactoryTest<LabOrderSearchFactory, LabOrderSearch> {

    @Override
    protected LabOrderSearchFactory createRepositoryFactory() {
        return new DefaultLabOrderSearchFactory();
    }

    @Override
    protected Class<LabOrderSearch> getRepositoryClass() {
        return LabOrderSearch.class;
    }
}
