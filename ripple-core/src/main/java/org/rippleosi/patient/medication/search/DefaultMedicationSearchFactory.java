package org.rippleosi.patient.medication.search;

import org.rippleosi.common.search.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultMedicationSearchFactory extends AbstractRepositoryFactory<MedicationSearch> implements MedicationSearchFactory {

    @Override
    protected MedicationSearch defaultRepository() {
        return new NotConfiguredMedicationSearch();
    }

    @Override
    protected Class<MedicationSearch> repositoryClass() {
        return MedicationSearch.class;
    }
}
