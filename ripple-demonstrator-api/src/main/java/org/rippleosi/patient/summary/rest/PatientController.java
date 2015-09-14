package org.rippleosi.patient.summary.rest;

import java.util.List;

import org.rippleosi.patient.summary.model.PatientDetails;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.rippleosi.patient.summary.search.PatientSearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientSearchFactory patientSearchFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<PatientSummary> findAllPatients(@RequestParam(required = false) String source) {

        PatientSearch patientSearch = patientSearchFactory.select(source);

        return patientSearch.findAllPatients();
    }

    @RequestMapping(value = "/{patientId}", method = RequestMethod.GET)
    public PatientDetails findPatientSummary(@PathVariable("patientId") String patientId,
                                             @RequestParam(required = false) String source) {
        PatientSearch patientSearch = patientSearchFactory.select(source);

        return patientSearch.findPatient(patientId);
    }
}
