/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
        List<LabResultSummary> results = labResultSearch.findAllLabResults(patientId);

        LabResultSearch openEhrSearch = labResultSearchFactory.select("Marand");
        results.addAll(openEhrSearch.findAllLabResults(patientId));

        return results;
    }

    @RequestMapping(value = "/{resultId}", method = RequestMethod.GET)
    public LabResultDetails findLabResult(@PathVariable("patientId") String patientId,
                                          @PathVariable("resultId") String resultId,
                                          @RequestParam(required = false) String source) {
        LabResultSearch labResultSearch = labResultSearchFactory.select(source);

        return labResultSearch.findLabResult(patientId, resultId);
    }
}
