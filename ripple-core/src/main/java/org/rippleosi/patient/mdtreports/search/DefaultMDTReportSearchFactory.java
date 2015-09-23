package org.rippleosi.patient.mdtreports.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultMDTReportSearchFactory extends AbstractRepositoryFactory<MDTReportSearch> implements MDTReportSearchFactory {

    @Override
    protected MDTReportSearch defaultRepository() {
        return new NotConfiguredMDTReportSearch();
    }

    @Override
    protected Class<MDTReportSearch> repositoryClass() {
        return MDTReportSearch.class;
    }
}
