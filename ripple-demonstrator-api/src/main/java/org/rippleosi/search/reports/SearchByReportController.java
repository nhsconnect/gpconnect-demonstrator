/*
 *   Copyright 2015 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package org.rippleosi.search.reports;

import java.util.List;

import org.rippleosi.common.types.RepoSource;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.rippleosi.patient.summary.search.PatientSearchFactory;
import org.rippleosi.search.patient.stats.model.SearchTableResults;
import org.rippleosi.search.patient.stats.PatientStatsSearch;
import org.rippleosi.search.patient.stats.PatientStatsSearchFactory;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;
import org.rippleosi.search.reports.graph.model.ReportGraphResults;
import org.rippleosi.search.reports.graph.ReportGraphSearch;
import org.rippleosi.search.reports.graph.ReportGraphSearchFactory;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.rippleosi.search.reports.table.ReportTableSearch;
import org.rippleosi.search.reports.table.ReportTableSearchFactory;
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
    public ReportGraphResults findReportGraphData(@RequestParam(required = false) String source,
                                                  @RequestBody ReportGraphQuery graphQuery) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        ReportGraphSearch search = reportGraphSearchFactory.select(sourceType);

        return search.findPatientDemographicsByQuery(graphQuery);
    }

    @RequestMapping(value = "/table", method = RequestMethod.POST)
    public SearchTableResults findReportTableData(@RequestParam(required = false) String patientSource,
                                                  @RequestParam(required = false) String patientDataSource,
                                                  @RequestBody ReportTableQuery tableQuery) {
        // retrieve all nhsNumbers associated with the query
        final RepoSource patientDataSourceType = RepoSourceType.fromString(patientDataSource);
        ReportTableSearch reportTableSearch = reportTableSearchFactory.select(patientDataSourceType);
        List<String> nhsNumbers = reportTableSearch.findAllPatientsByQuery(tableQuery);

        // do a local search for a page of results
        final RepoSource patientSourceType = RepoSourceType.fromString(patientSource);
        PatientSearch patientSearch = patientSearchFactory.select(patientSourceType);
        List<PatientSummary> patientSummaries = patientSearch.findAllMatchingPatients(nhsNumbers, tableQuery);

        // search and aggregate the associated data
        PatientStatsSearch patientStatsSearch = patientStatsSearchFactory.select(patientDataSourceType);
        SearchTableResults associatedData = patientStatsSearch.findAssociatedPatientData(tableQuery, patientSummaries);

        // finally, set the total
        Integer totalPatients = nhsNumbers.size();
        associatedData.setTotalPatients(String.valueOf(totalPatients));

        return associatedData;
    }
}
