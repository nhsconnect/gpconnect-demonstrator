package org.rippleosi.patient.labresults.rest;

import java.util.List;

import org.rippleosi.patient.labresults.model.LabResultDetails;
import org.rippleosi.patient.labresults.model.LabResultSummary;
import org.rippleosi.patient.labresults.search.LabResultSearch;
import org.rippleosi.patient.labresults.search.LabResultSearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/labresults")
public class LabResultsController {

    @Autowired
    private LabResultSearchFactory labResultSearchFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<LabResultSummary> findAllLabResults(@PathVariable("patientId") String patientId,
                                                    @RequestParam(required = false) String source) {
        LabResultSearch labResultSearch = labResultSearchFactory.select(source);

        return labResultSearch.findAllLabResults(patientId);
    }

    @RequestMapping(value = "/{resultId}", method = RequestMethod.GET)
    public LabResultDetails findLabResult(@PathVariable("patientId") String patientId,
                                          @PathVariable("resultId") String resultId,
                                          @RequestParam(required = false) String source) {
        LabResultSearch labResultSearch = labResultSearchFactory.select(source);

        return labResultSearch.findLabResult(patientId, resultId);
    }
}
