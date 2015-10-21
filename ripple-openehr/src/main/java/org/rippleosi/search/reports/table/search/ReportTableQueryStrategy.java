package org.rippleosi.search.reports.table.search;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
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
            "and a_a/data[at0001]/items[at0002]/value/defining_code/code_string matches {snomedCode} " +
            "and a_a/data[at0001]/items[at0002]/value/defining_code/terminology_id/value='SNOMED-CT' " +
            "and e/ehr_status/other_details/items[openEHR-EHR-CLUSTER.person_anonymised_parent.v1]/items[at0014]/value/value>='" + yearFrom + "' " +
            "and e/ehr_status/other_details/items[openEHR-EHR-CLUSTER.person_anonymised_parent.v1]/items[at0014]/value/value<='" + yearTo + "'";
    }

    // needed so that braces can be used in the URI (expansion during the RestTemplate exchange would fail otherwise)
    public Map<String, String> getUriVariables() {
        Map<String, String> uriVars = new HashMap<>();
        uriVars.put("openEHRTemplate", "{'Problem List'}");
        uriVars.put("snomedCode", "{'22298006'}");
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
