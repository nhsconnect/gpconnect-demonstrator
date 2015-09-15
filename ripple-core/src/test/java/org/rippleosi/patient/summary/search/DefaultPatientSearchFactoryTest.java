package org.rippleosi.patient.summary.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.search.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPatientSearchFactoryTest extends AbstractRepositoryFactoryTest<PatientSearchFactory,PatientSearch> {

    @Override
    protected PatientSearchFactory createRepositoryFactory() {
        return new DefaultPatientSearchFactory();
    }

    @Override
    protected Class<PatientSearch> getRepositoryClass() {
        return PatientSearch.class;
    }
}
