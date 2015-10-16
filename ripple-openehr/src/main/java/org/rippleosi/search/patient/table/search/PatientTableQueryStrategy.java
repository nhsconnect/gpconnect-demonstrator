package org.rippleosi.search.patient.table.search;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.C4HUriQueryStrategy;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.common.model.OpenEHRDatesAndCountsResponse;
import org.rippleosi.search.common.model.SearchTablePatientDetails;
import org.rippleosi.search.patient.table.model.PatientTableQuery;
import org.rippleosi.search.reports.table.model.ReportTableResults;
import org.rippleosi.search.setting.table.model.OpenEHRSettingRequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PatientTableQueryStrategy implements C4HUriQueryStrategy<OpenEHRDatesAndCountsResponse[], ReportTableResults> {

    @Value("${c4hOpenEHR.address}")
    private String c4hOpenEHRAddress;

    @Value("${c4hOpenEHR.subjectNamespace}")
    private String externalNamespace;

    private List<PatientSummary> patientSummaries;
    private PatientTableQuery tableQuery;

    public void setPatientSummaries(List<PatientSummary> patientSummaries) {
        this.patientSummaries = patientSummaries;
    }

    public void setTableQuery(PatientTableQuery tableQuery) {
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
        OpenEHRSettingRequestBody body = new OpenEHRSettingRequestBody();

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
    public ReportTableResults transform(OpenEHRDatesAndCountsResponse[] resultSet) {
        ReportTableResults results = new ReportTableResults();
        List<SearchTablePatientDetails> details = CollectionUtils.collect(patientSummaries,
                                                                          new PatientTablePatientDetailsTransformer(resultSet),
                                                                          new ArrayList<>());
        results.setPatientDetails(details);
        return results;
    }
}
