package uk.gov.hscic.patient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.patient.summary.model.PatientDetails;
import uk.gov.hscic.patient.summary.model.PatientQueryParams;
import uk.gov.hscic.patient.summary.model.PatientSummary;
import uk.gov.hscic.patient.summary.search.PatientSearch;

@RestController
@RequestMapping("/patients")
public class PatientsRestController {

    @Autowired
    PatientSearch patientSearch;

    @RequestMapping(method = RequestMethod.GET)
    public List<PatientSummary> findAllPatients(@RequestParam(required = false) String source) {
        return patientSearch.findAllPatients();
    }

    @RequestMapping(value = "/{patientId}", method = RequestMethod.GET)
    public PatientDetails findPatientByNHSNumber(@PathVariable("patientId") String patientId, @RequestParam(required = false) String source) {
        return patientSearch.findPatient(patientId);
    }

    @RequestMapping(value = "/advancedSearch", method = RequestMethod.POST)
    public List<PatientSummary> findPatientsByQueryObject(@RequestParam(required = false) String source, @RequestBody PatientQueryParams queryParams) {
        return patientSearch.findPatientsByQueryObject(queryParams);
    }
}