package org.rippleosi.search.reports;

import java.util.List;

import org.rippleosi.search.reports.graph.model.ReportGraphDemographicSummary;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;
import org.rippleosi.search.reports.graph.search.ReportGraphSearch;
import org.rippleosi.search.reports.graph.search.ReportGraphSearchFactory;
import org.rippleosi.search.reports.table.model.ReportTablePatientDetails;
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
public class ReportsController {

    @Autowired
    private ReportGraphSearchFactory reportGraphSearchFactory;

    @Autowired
    private ReportTableSearchFactory reportTableSearchFactory;

    @RequestMapping(value = "/chart", method = RequestMethod.POST)
    public ReportGraphDemographicSummary getGraphReportByType(@RequestParam(required = false) String source,
                                                              @RequestBody ReportGraphQuery graphQuery) {
        ReportGraphSearch search = reportGraphSearchFactory.select(source);
        return search.findPatientDemographicsByQuery(graphQuery);
    }

    @RequestMapping(value = "/table", method = RequestMethod.POST)
    public List<ReportTablePatientDetails> getTableReportByType(@RequestParam(required = false) String source,
                                                                @RequestBody ReportTableQuery tableQuery) {
        ReportTableSearch search = reportTableSearchFactory.select(source);
        return search.findAllPatientsByQuery(tableQuery);
    }
}
