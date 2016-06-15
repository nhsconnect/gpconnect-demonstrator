package uk.gov.hscic.medication.order.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultMedicationOrderSearchFactory extends AbstractRepositoryFactory<MedicationOrderSearch> implements MedicationOrderSearchFactory {

    @Override
    protected MedicationOrderSearch defaultRepository() {
        return new NotConfiguredMedicationOrderSearch();
    }

    @Override
    protected Class<MedicationOrderSearch> repositoryClass() {
        return MedicationOrderSearch.class;
    }
}