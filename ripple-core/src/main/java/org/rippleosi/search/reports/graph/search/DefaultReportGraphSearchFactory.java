package org.rippleosi.search.reports.graph.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultReportGraphSearchFactory extends AbstractRepositoryFactory<ReportGraphSearch>
    implements ReportGraphSearchFactory {

    @Override
    protected ReportGraphSearch defaultRepository() {
        return new NotConfiguredReportGraphSearch();
    }

    @Override
    protected Class<ReportGraphSearch> repositoryClass() {
        return ReportGraphSearch.class;
    }
}
