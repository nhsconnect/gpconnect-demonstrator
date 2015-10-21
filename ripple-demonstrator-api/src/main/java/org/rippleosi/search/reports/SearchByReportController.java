package org.rippleosi.search.reports;

import java.util.List;

import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.rippleosi.patient.summary.search.PatientSearchFactory;
import org.rippleosi.search.patient.stats.model.SearchTableResults;
import org.rippleosi.search.patient.stats.search.PatientStatsSearch;
import org.rippleosi.search.patient.stats.search.PatientStatsSearchFactory;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;
import org.rippleosi.search.reports.graph.model.ReportGraphResults;
import org.rippleosi.search.reports.graph.search.ReportGraphSearch;
import org.rippleosi.search.reports.graph.search.ReportGraphSearchFactory;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.rippleosi.search.reports.table.search.ReportTableSearch;
import org.rippleosi.search.reports.table.search.ReportTableSearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search/reports")
public class SearchByReportController {

    @Autowired
    private ReportGraphSearchFactory reportGraphSearchFactory;

    @Autowired
    private ReportTableSearchFactory reportTableSearchFactory;

    @Autowired
    private PatientSearchFactory patientSearchFactory;

    @Autowired
    private PatientStatsSearchFactory patientStatsSearchFactory;

    @RequestMapping(value = "/chart", method = RequestMethod.POST)
    public ReportGraphResults getReportGraph(@RequestParam(required = false) String source,
                                             @RequestBody ReportGraphQuery graphQuery) {
        ReportGraphSearch search = reportGraphSearchFactory.select(source);
        return search.findPatientDemographicsByQuery(graphQuery);
    }

    @RequestMapping(value = "/table", method = RequestMethod.POST)
    public SearchTableResults getReportTable(@RequestParam(required = false) String patientSource,
                                             @RequestParam(required = false) String patientDataSource,
                                             @RequestBody ReportTableQuery tableQuery) {
        // retrieve all nhsNumbers associated with the query
        ReportTableSearch reportTableSearch = reportTableSearchFactory.select(patientDataSource);
        List<String> nhsNumbers = reportTableSearch.findAllPatientsByQuery(tableQuery);

        // do a local search for a page of results
        PatientSearch patientSearch = patientSearchFactory.select(patientSource);
        List<PatientSummary> patientSummaries = patientSearch.findAllMatchingPatients(nhsNumbers, tableQuery);

        // search and aggregate the associated data
        PatientStatsSearch patientStatsSearch = patientStatsSearchFactory.select(patientDataSource);
        SearchTableResults associatedData = patientStatsSearch.findAssociatedPatientData(tableQuery, patientSummaries);

        // finally, set the total
        int totalPatients = nhsNumbers.size();
        associatedData.setTotalPatients(String.valueOf(totalPatients));

        return associatedData;
    }
}
