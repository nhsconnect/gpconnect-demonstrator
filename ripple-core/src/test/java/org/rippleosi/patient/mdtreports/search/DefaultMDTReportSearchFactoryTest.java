package org.rippleosi.patient.mdtreports.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultMDTReportSearchFactoryTest extends AbstractRepositoryFactoryTest<MDTReportSearchFactory, MDTReportSearch> {

    @Override
    protected MDTReportSearchFactory createRepositoryFactory() {
        return new DefaultMDTReportSearchFactory();
    }

    @Override
    protected Class<MDTReportSearch> getRepositoryClass() {
        return MDTReportSearch.class;
    }
}
