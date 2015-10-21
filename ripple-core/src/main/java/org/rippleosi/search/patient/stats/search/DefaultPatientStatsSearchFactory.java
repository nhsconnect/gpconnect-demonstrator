package org.rippleosi.search.patient.stats.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultPatientStatsSearchFactory extends AbstractRepositoryFactory<PatientStatsSearch>
    implements PatientStatsSearchFactory {

    @Override
    protected PatientStatsSearch defaultRepository() {
        return new NotConfiguredPatientStatsSearch();
    }

    @Override
    protected Class<PatientStatsSearch> repositoryClass() {
        return PatientStatsSearch.class;
    }
}
