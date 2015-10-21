package org.rippleosi.search.patient;

import java.util.List;

import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.rippleosi.patient.summary.search.PatientSearchFactory;
import org.rippleosi.search.patient.stats.model.SearchTableResults;
import org.rippleosi.search.patient.stats.model.PatientTableQuery;
import org.rippleosi.search.patient.stats.search.PatientStatsSearch;
import org.rippleosi.search.patient.stats.search.PatientStatsSearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search/patient")
public class SearchByPatientController {

    @Autowired
    private PatientSearchFactory patientSearchFactory;

    @Autowired
    private PatientStatsSearchFactory patientStatsSearchFactory;

    @RequestMapping(value = "/table", method = RequestMethod.POST)
    public SearchTableResults getPatientTable(@RequestParam(required = false) String patientSource,
                                              @RequestParam(required = false) String patientDataSource,
                                              @RequestBody PatientTableQuery tableQuery) {
        PatientSearch patientSearch = patientSearchFactory.select(patientSource);
        List<PatientSummary> patientSummaries = patientSearch.findPatientsByQuery(tableQuery);

        PatientStatsSearch tableSearch = patientStatsSearchFactory.select(patientDataSource);
        SearchTableResults results = tableSearch.findAssociatedPatientData(tableQuery, patientSummaries);

        Long total = patientSearch.countPatientsByQuery(tableQuery);
        results.setTotalPatients(String.valueOf(total));

        return results;
    }
}
