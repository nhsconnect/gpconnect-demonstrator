package org.rippleosi.search.reports.table.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;

public class DefaultReportTableGraphSearchFactory extends AbstractRepositoryFactory<ReportTableSearch>
    implements ReportTableSearchFactory {

    @Override
    protected ReportTableSearch defaultRepository() {
        return new NotConfiguredReportTableSearch();
    }

    @Override
    protected Class<ReportTableSearch> repositoryClass() {
        return ReportTableSearch.class;
    }
}
