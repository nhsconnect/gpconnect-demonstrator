package org.rippleosi.patient.summary.search;

import org.rippleosi.common.search.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultPatientSearchFactory extends AbstractRepositoryFactory<PatientSearch> implements PatientSearchFactory {

    @Override
    protected PatientSearch defaultRepository() {
        return new NotConfiguredPatientSearch();
    }

    @Override
    protected Class<PatientSearch> repositoryClass() {
        return PatientSearch.class;
    }
}
