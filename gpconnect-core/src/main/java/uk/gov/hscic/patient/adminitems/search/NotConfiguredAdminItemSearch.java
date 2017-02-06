package uk.gov.hscic.patient.adminitems.search;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.adminitems.model.AdminItemListHTML;

import java.util.Date;
import java.util.List;

public class NotConfiguredAdminItemSearch implements AdminItemSearch {

    @Override
    public RepoSource getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<AdminItemListHTML> findAllAdminItemHTMLTables(final String patientId,Date fromDate, Date toDate) {
        throw ConfigurationException.unimplementedTransaction(AdminItemSearch.class);
    }
}
