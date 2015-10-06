package org.rippleosi.search.reports.table.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.search.reports.table.model.ReportTablePatientDetails;
import org.rippleosi.search.reports.table.model.ReportTableQuery;

public interface ReportTableSearch extends Repository {

    List<ReportTablePatientDetails> findAllPatientsByQuery(ReportTableQuery tableQuery);
}
