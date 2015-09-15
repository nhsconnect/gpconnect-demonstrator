package org.rippleosi.patient.problems.search;

import org.rippleosi.common.search.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultProblemSearchFactory extends AbstractRepositoryFactory<ProblemSearch> implements ProblemSearchFactory {

    @Override
    protected ProblemSearch defaultRepository() {
        return new NotConfiguredProblemSearch();
    }

    @Override
    protected Class<ProblemSearch> repositoryClass() {
        return ProblemSearch.class;
    }
}
