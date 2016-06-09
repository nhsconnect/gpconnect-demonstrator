package uk.gov.hscic.practitioner.search;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.practitioner.model.PractitionerDetails;

public class NotConfiguredPractitionerSearch implements PractitionerSearch {

    @Override
    public RepoSource getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public PractitionerDetails findPractitionerDetails(final String practitionerId) {
        throw ConfigurationException.unimplementedTransaction(PractitionerSearch.class);
    }
}
