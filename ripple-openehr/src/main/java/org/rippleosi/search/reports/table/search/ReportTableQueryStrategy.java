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

package org.rippleosi.search.reports.table.search;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.search.reports.table.model.ReportTableQuery;

public class ReportTableQueryStrategy extends AbstractQueryStrategy<List<String>> {

    private ReportTableQuery tableQuery;
    private Integer yearFrom;
    private Integer yearTo;

    public ReportTableQueryStrategy(ReportTableQuery tableQuery) {
        super(null);
        this.tableQuery = tableQuery;
        calculateYearFromAndTo();
    }
    
    private void calculateYearFromAndTo() {
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearFrom = currentYear - Integer.valueOf(tableQuery.getAgeTo());
        yearTo = currentYear - Integer.valueOf(tableQuery.getAgeFrom());
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        // TODO - add reportType and SNOMED CT code to the AQL (from tableQuery)
        return "select e/ehr_status/subject/external_ref/id/value as NHSNumber " +
            "from EHR e " +
            "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
            "contains EVALUATION a_a[openEHR-EHR-EVALUATION.problem_diagnosis.v1] " +
            "where a/name/value matches {openEHRTemplate} " +
            "and a_a/data[at0001]/items[at0002]/value/value matches {searchString}" +
            "and e/ehr_status/other_details/items[openEHR-EHR-CLUSTER.person_anonymised_parent.v1]/items[at0014]/value/value>='" + yearFrom + "' " +
            "and e/ehr_status/other_details/items[openEHR-EHR-CLUSTER.person_anonymised_parent.v1]/items[at0014]/value/value<='" + yearTo + "'";
    }

    // needed so that braces can be used in the URI (expansion during the RestTemplate exchange would fail otherwise)
    public Map<String, String> getUriVariables() {
        Map<String, String> uriVars = new HashMap<>();
        uriVars.put("openEHRTemplate", "{'Problem List'}");
        uriVars.put("searchString", "{'" + StringUtils.strip(tableQuery.getSearchString()) + "'}");
        return uriVars;
    }

    @Override
    public List<String> transform(List<Map<String, Object>> resultSet) {
        List<String> nhsNumbers = new ArrayList<>();

        nhsNumbers.addAll(resultSet.stream()
                                   .map(entry -> (String) entry.get("NHSNumber"))
                                   .collect(Collectors.toList()));

        return nhsNumbers;
    }
}
