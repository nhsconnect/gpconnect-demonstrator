package org.rippleosi.search.setting.table.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.setting.table.model.SettingTableResults;
import org.springframework.stereotype.Service;

@Service
public class OpenEHRSettingTableSearch extends AbstractOpenEhrService implements SettingTableSearch {

    @Override
    public SettingTableResults findAssociatedPatientData(List<PatientSummary> patientSummaries) {
        SettingTableQueryStrategy queryStrategy = new SettingTableQueryStrategy(patientSummaries);
        return queryStrategy.transform(null);

        // TODO - delete the invocation of transform() and uncomment the line below
//        return findData(queryStrategy);
    }
}
