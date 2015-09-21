package org.rippleosi.patient.medication.store;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultMedicationStoreFactoryTest extends AbstractRepositoryFactoryTest<MedicationStoreFactory, MedicationStore> {

    @Override
    protected MedicationStoreFactory createRepositoryFactory() {
        return new DefaultMedicationStoreFactory();
    }

    @Override
    protected Class<MedicationStore> getRepositoryClass() {
        return MedicationStore.class;
    }
}
