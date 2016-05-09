package uk.gov.hscic.patient.keyindicator.search;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.adminitems.model.AdminItemListHTML;
import uk.gov.hscic.patient.adminitems.search.AdminItemSearch;
import uk.gov.hscic.patient.keyindicator.model.KeyIndicatorListHTML;

import java.util.List;

public class NotConfiguredKeyIndicatorSearch implements KeyIndicatorSearch {

    @Override
    public RepoSource getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<KeyIndicatorListHTML> findAllKeyIndicatorHTMLTables(final String patientId) {
        throw ConfigurationException.unimplementedTransaction(KeyIndicatorSearch.class);
    }
}
