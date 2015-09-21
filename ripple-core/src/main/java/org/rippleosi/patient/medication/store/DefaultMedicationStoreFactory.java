package org.rippleosi.patient.medication.store;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultMedicationStoreFactory extends AbstractRepositoryFactory<MedicationStore> implements MedicationStoreFactory {

    @Override
    protected MedicationStore defaultRepository() {
        return new NotConfiguredMedicationStore();
    }

    @Override
    protected Class<MedicationStore> repositoryClass() {
        return MedicationStore.class;
    }
}
