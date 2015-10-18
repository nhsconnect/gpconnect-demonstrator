package org.rippleosi.search.patient;

import java.util.List;

import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.rippleosi.patient.summary.search.PatientSearchFactory;
import org.rippleosi.search.patient.table.model.PatientTableQuery;
import org.rippleosi.search.patient.table.search.PatientTableSearch;
import org.rippleosi.search.patient.table.search.PatientTableSearchFactory;
import org.rippleosi.search.reports.table.model.ReportTableResults;
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
    private PatientTableSearchFactory patientTableSearchFactory;

    @RequestMapping(value = "/table", method = RequestMethod.POST)
    public ReportTableResults getPatientTable(@RequestParam(required = false) String source,
                                              @RequestBody PatientTableQuery tableQuery) {
        PatientSearch patientSearch = patientSearchFactory.select(source);
        List<PatientSummary> patientSummaries = patientSearch.findPatientsByQuery(tableQuery);

        PatientTableSearch tableSearch = patientTableSearchFactory.select(source);
        ReportTableResults results = tableSearch.findAssociatedPatientData(tableQuery, patientSummaries);

        Long total = patientSearch.countPatientsByQuery(tableQuery);
        results.setTotalPatients(String.valueOf(total));

        return results;
    }
}
