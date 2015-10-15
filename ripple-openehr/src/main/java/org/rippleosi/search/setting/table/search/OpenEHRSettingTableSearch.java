package org.rippleosi.search.setting.table.search;

import java.util.List;

import org.rippleosi.common.service.AbstractC4HReportingService;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.setting.table.model.SettingTableQuery;
import org.rippleosi.search.setting.table.model.SettingTableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenEHRSettingTableSearch extends AbstractC4HReportingService implements SettingTableSearch {

    @Autowired
    private SettingTableQueryStrategy queryStrategy;

    @Override
    public SettingTableResults findAssociatedPatientData(SettingTableQuery tableQuery, List<PatientSummary> patientSummaries) {
        queryStrategy.setPatientSummaries(patientSummaries);
        queryStrategy.setTableQuery(tableQuery);
        return findTableData(queryStrategy);
    }
}
