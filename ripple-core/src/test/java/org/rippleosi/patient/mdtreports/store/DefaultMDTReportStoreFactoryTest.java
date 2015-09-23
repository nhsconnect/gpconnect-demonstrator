package org.rippleosi.patient.mdtreports.store;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultMDTReportStoreFactoryTest extends AbstractRepositoryFactoryTest<MDTReportStoreFactory, MDTReportStore> {

    @Override
    protected MDTReportStoreFactory createRepositoryFactory() {
        return new DefaultMDTReportStoreFactory();
    }

    @Override
    protected Class<MDTReportStore> getRepositoryClass() {
        return MDTReportStore.class;
    }
}
