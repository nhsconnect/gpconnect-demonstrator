package org.rippleosi.search.reports;

import org.rippleosi.search.reports.graph.model.ReportGraphQuery;
import org.rippleosi.search.reports.graph.model.ReportGraphResults;
import org.rippleosi.search.reports.graph.search.ReportGraphSearch;
import org.rippleosi.search.reports.graph.search.ReportGraphSearchFactory;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.rippleosi.search.reports.table.model.ReportTableResults;
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

    @RequestMapping(value = "/chart", method = RequestMethod.POST)
    public ReportGraphResults getReportGraph(@RequestParam(required = false) String source,
                                             @RequestBody ReportGraphQuery graphQuery) {
        ReportGraphSearch search = reportGraphSearchFactory.select(source);
        return search.findPatientDemographicsByQuery(graphQuery);
    }

    @RequestMapping(value = "/table", method = RequestMethod.POST)
    public ReportTableResults getReportTable(@RequestParam(required = false) String source,
                                             @RequestBody ReportTableQuery tableQuery) {
        ReportTableSearch search = reportTableSearchFactory.select(source);
        return search.findAllPatientsByQuery(tableQuery);
    }
}
