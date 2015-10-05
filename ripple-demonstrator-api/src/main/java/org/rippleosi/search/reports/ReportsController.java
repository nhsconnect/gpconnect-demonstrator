package org.rippleosi.search.reports;

import java.util.List;

import org.rippleosi.search.reports.graph.model.ReportGraphPatientSummary;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;
import org.rippleosi.search.reports.table.model.ReportTablePatientSummary;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search/reports")
public class ReportsController {

    @RequestMapping(value = "/chart", method = RequestMethod.GET)
    public List<ReportGraphPatientSummary> getGraphReportByType(@RequestBody ReportGraphQuery query) {
        return null;
    }

    @RequestMapping(value = "/table", method = RequestMethod.GET)
    public List<ReportTablePatientSummary> getTableReportByType(@RequestBody ReportTableQuery query) {
        return null;
    }
}
