package uk.gov.hscic.patient.patientsummary.search;

import uk.gov.hscic.common.repo.RepositoryFactory;

@FunctionalInterface
public interface PatientSummarySearchFactory extends RepositoryFactory<PatientSummarySearch> {
    
}
