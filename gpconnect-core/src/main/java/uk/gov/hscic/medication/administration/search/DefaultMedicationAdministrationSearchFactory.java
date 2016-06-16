package uk.gov.hscic.medication.administration.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultMedicationAdministrationSearchFactory extends AbstractRepositoryFactory<MedicationAdministrationSearch> implements MedicationAdministrationSearchFactory {

    @Override
    protected MedicationAdministrationSearch defaultRepository() {
        return new NotConfiguredMedicationAdministrationSearch();
    }

    @Override
    protected Class<MedicationAdministrationSearch> repositoryClass() {
        return MedicationAdministrationSearch.class;
    }
}
