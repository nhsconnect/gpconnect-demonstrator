package org.rippleosi.search.setting.table.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.common.model.SearchTablePatientDetails;
import org.rippleosi.search.setting.table.model.SettingTableResults;

public class SettingTableQueryStrategy extends AbstractQueryStrategy<SettingTableResults> {

    private List<PatientSummary> patientSummaries;

    public SettingTableQueryStrategy(List<PatientSummary> patientSummaries) {
        super(null);
        this.patientSummaries = patientSummaries;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        // TODO - implement
        return null;
    }

    @Override
    public SettingTableResults transform(List<Map<String, Object>> resultSet) {
        SettingTableResults results = new SettingTableResults();
        List<SearchTablePatientDetails> details = CollectionUtils.collect(patientSummaries,
                                                                          new SettingTablePatientDetailsTransformer(),
                                                                          new ArrayList<>());
        results.setPatientDetails(details);
        results.setTotalPatients(String.valueOf(details.size()));
        return results;
    }
}
