package uk.gov.hscic.medication.dispense.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;
@Service
public class DefaultMedicationDispenseSearchFactory extends AbstractRepositoryFactory<MedicationDispenseSearch> implements MedicationDispenseSearchFactory {

    @Override
    protected MedicationDispenseSearch defaultRepository() {
        return new NotConfiguredMedicationDispenseSearch();
    }

    @Override
    protected Class<MedicationDispenseSearch> repositoryClass() {
        return MedicationDispenseSearch.class;
    }
}