package org.rippleosi.search.setting;

import java.util.List;

import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.rippleosi.patient.summary.search.PatientSearchFactory;
import org.rippleosi.search.patient.stats.model.SearchTableResults;
import org.rippleosi.search.patient.stats.search.PatientStatsSearch;
import org.rippleosi.search.patient.stats.search.PatientStatsSearchFactory;
import org.rippleosi.search.setting.table.model.SettingTableQuery;
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
    private PatientStatsSearchFactory patientStatsSearchFactory;

    @RequestMapping(value = "/table", method = RequestMethod.POST)
    public SearchTableResults getSettingTable(@RequestParam(required = false) String patientSource,
                                               @RequestParam(required = false) String patientDataSource,
                                               @RequestBody SettingTableQuery tableQuery) {
        PatientSearch patientSearch = patientSearchFactory.select(patientSource);
        List<PatientSummary> patientSummaries = patientSearch.findAllPatientsByDepartment(tableQuery);

        PatientStatsSearch settingSearch = patientStatsSearchFactory.select(patientDataSource);
        SearchTableResults results = settingSearch.findAssociatedPatientData(tableQuery, patientSummaries);

        Integer countByDepartment = patientSearch.findPatientCountByDepartment(tableQuery.getSearchString());
        results.setTotalPatients(String.valueOf(countByDepartment));

        return results;
    }
}
