package uk.gov.hscic.patient.patientsummary.search;

import uk.gov.hscic.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultPatientSummarySearchFactory extends AbstractRepositoryFactory<PatientSummarySearch> implements PatientSummarySearchFactory {

    @Override
    protected PatientSummarySearch defaultRepository() {
        return new NotConfiguredPatientSummarySearch();
    }

    @Override
    protected Class<PatientSummarySearch> repositoryClass() {
        return PatientSummarySearch.class;
    }
}
