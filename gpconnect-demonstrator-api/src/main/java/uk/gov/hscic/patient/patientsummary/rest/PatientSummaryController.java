package uk.gov.hscic.patient.patientsummary.rest;

import java.util.List;

import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHTML;
import uk.gov.hscic.patient.patientsummary.search.PatientSummarySearch;
import uk.gov.hscic.patient.patientsummary.search.PatientSummarySearchFactory;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/patientsummary")
public class PatientSummaryController {

    @Autowired
    private PatientSummarySearchFactory patientSummarySearchFactory;

    @RequestMapping(value="/htmlTables", method = RequestMethod.GET)
    public List<PatientSummaryListHTML> findAllPatientSummaryHTMLTables(@PathVariable("patientId") String patientId,
                                                @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final PatientSummarySearch patientSummarySearch = patientSummarySearchFactory.select(sourceType);

        return patientSummarySearch.findAllPatientSummaryHTMLTables(patientId);
    }
}
