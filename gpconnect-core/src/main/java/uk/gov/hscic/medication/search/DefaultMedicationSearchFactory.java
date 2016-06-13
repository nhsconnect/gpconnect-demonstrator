package uk.gov.hscic.medication.search;

import uk.gov.hscic.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

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
