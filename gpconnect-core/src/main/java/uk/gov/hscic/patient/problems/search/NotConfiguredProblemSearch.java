package uk.gov.hscic.patient.problems.search;

import java.util.List;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.problems.model.HTMLProblemObject;

/**
 */
public class NotConfiguredProblemSearch implements ProblemSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<HTMLProblemObject> findAllProblemHTMLTables(final String patientId) {
        throw ConfigurationException.unimplementedTransaction(ProblemSearch.class);
    }

}
