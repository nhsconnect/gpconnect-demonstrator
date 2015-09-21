package org.rippleosi.patient.medication.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultMedicationSearchFactoryTest extends AbstractRepositoryFactoryTest<MedicationSearchFactory, MedicationSearch> {

    @Override
    protected MedicationSearchFactory createRepositoryFactory() {
        return new DefaultMedicationSearchFactory();
    }

    @Override
    protected Class<MedicationSearch> getRepositoryClass() {
        return MedicationSearch.class;
    }
}
