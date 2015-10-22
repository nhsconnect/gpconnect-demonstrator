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

package org.rippleosi.search.patient.stats.search;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.C4HUriQueryStrategy;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.common.model.OpenEHRDatesAndCountsResponse;
import org.rippleosi.search.common.model.PageableTableQuery;
import org.rippleosi.search.patient.stats.model.OpenEHRPatientStatsRequestBody;
import org.rippleosi.search.patient.stats.model.SearchTablePatientDetails;
import org.rippleosi.search.patient.stats.model.SearchTableResults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PatientStatsQueryStrategy implements C4HUriQueryStrategy<OpenEHRDatesAndCountsResponse[], SearchTableResults> {

    @Value("${c4hOpenEHR.address}")
    private String c4hOpenEHRAddress;

    @Value("${c4hOpenEHR.subjectNamespace}")
    private String externalNamespace;

    private List<PatientSummary> patientSummaries;
    private PageableTableQuery tableQuery;

    public void setPatientSummaries(List<PatientSummary> patientSummaries) {
        this.patientSummaries = patientSummaries;
    }

    public void setTableQuery(PageableTableQuery tableQuery) {
        this.tableQuery = tableQuery;
    }

    @Override
    public UriComponents getQueryUriComponents() {
        Integer pageNumber = Integer.valueOf(tableQuery.getPageNumber()) - 1;
        Integer pageSize = 15;

        return UriComponentsBuilder
            .fromHttpUrl(c4hOpenEHRAddress + "/view/rippleDash")
            .queryParam("orderBy", "NHSNumber")
            .queryParam("descending", tableQuery.getOrderType().equals("DESC"))
            .queryParam("offset", pageNumber * pageSize)
            .queryParam("limit", pageSize)
            .build();
    }

    @Override
    public Object getRequestBody() {
        OpenEHRPatientStatsRequestBody body = new OpenEHRPatientStatsRequestBody();

        // create a CSV list of NHS Numbers for OpenEHR to query associated data
        StringJoiner csvBuilder = new StringJoiner(",");

        for (PatientSummary patient : patientSummaries) {
            csvBuilder.add(patient.getNhsNumber());
        }

        body.setExternalIds(csvBuilder.toString());
        body.setExternalNamespace(externalNamespace);
        return body;
    }

    @Override
    public SearchTableResults transform(OpenEHRDatesAndCountsResponse[] resultSet) {
        SearchTableResults results = new SearchTableResults();
        List<SearchTablePatientDetails> details = CollectionUtils.collect(patientSummaries,
                                                                          new PatientStatsPatientDetailsTransformer(resultSet),
                                                                          new ArrayList<>());
        results.setPatientDetails(details);
        return results;
    }
}
