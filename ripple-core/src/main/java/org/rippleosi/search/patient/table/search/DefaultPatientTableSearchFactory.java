package org.rippleosi.search.patient.table.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultPatientTableSearchFactory extends AbstractRepositoryFactory<PatientTableSearch>
    implements PatientTableSearchFactory {

    @Override
    protected PatientTableSearch defaultRepository() {
        return new NotConfiguredPatientTableSearch();
    }

    @Override
    protected Class<PatientTableSearch> repositoryClass() {
        return PatientTableSearch.class;
    }
}
