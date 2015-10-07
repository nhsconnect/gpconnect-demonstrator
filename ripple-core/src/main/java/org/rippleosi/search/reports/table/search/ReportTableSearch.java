package org.rippleosi.search.reports.table.search;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.rippleosi.search.reports.table.model.ReportTableResults;

public interface ReportTableSearch extends Repository {

    ReportTableResults findAllPatientsByQuery(ReportTableQuery tableQuery);
}
