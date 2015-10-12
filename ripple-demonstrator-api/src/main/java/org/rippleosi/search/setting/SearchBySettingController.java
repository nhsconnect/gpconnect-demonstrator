package org.rippleosi.search.setting;

import java.util.List;

import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.rippleosi.patient.summary.search.PatientSearchFactory;
import org.rippleosi.search.setting.table.model.SettingTableQuery;
import org.rippleosi.search.setting.table.model.SettingTableResults;
import org.rippleosi.search.setting.table.search.SettingTableSearch;
import org.rippleosi.search.setting.table.search.SettingTableSearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search/setting")
public class SearchBySettingController {

    @Autowired
    private PatientSearchFactory patientSearchFactory;

    @Autowired
    private SettingTableSearchFactory settingTableSearchFactory;

    @RequestMapping(value = "/table", method = RequestMethod.POST)
    public SettingTableResults getSettingTable(@RequestParam(required = false) String patientSource,
                                               @RequestParam(required = false) String patientDataSource,
                                               @RequestBody SettingTableQuery tableQuery) {
        PatientSearch patientSearch = patientSearchFactory.select(patientSource);
        List<PatientSummary> patientSummaries = patientSearch.findAllPatientsByDepartment(tableQuery);

        SettingTableSearch settingSearch = settingTableSearchFactory.select(patientDataSource);
        SettingTableResults results = settingSearch.findAssociatedPatientData(patientSummaries);

        Integer countByDepartment = patientSearch.findPatientCountByDepartment(tableQuery.getSearchString());
        results.setTotalPatients(countByDepartment.toString());

        return results;
    }
}
