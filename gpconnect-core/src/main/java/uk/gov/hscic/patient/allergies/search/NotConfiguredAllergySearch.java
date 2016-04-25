package uk.gov.hscic.patient.allergies.search;

import java.util.List;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.allergies.model.AllergyDetails;
import uk.gov.hscic.patient.allergies.model.AllergyHeadline;
import uk.gov.hscic.patient.allergies.model.AllergyListHTML;
import uk.gov.hscic.patient.allergies.model.AllergySummary;

/**
 */
public class NotConfiguredAllergySearch implements AllergySearch {

    @Override
    public RepoSource getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<AllergyListHTML> findAllAllergyHTMLTables(final String patientId) {
        throw ConfigurationException.unimplementedTransaction(AllergySearch.class);
    }
}
