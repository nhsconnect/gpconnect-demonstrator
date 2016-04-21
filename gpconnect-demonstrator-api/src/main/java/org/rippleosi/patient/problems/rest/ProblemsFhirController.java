/*
 *   Copyright 2015 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
package org.rippleosi.patient.problems.rest;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.formats.JsonParser;
import org.hl7.fhir.instance.model.Condition;
import org.rippleosi.common.types.RepoSource;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.patient.problems.search.ProblemSearch;
import org.rippleosi.patient.problems.search.ProblemSearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fhir/patients/{patientId}/conditions")
public class ProblemsFhirController {

    @Autowired
    private ProblemSearchFactory problemSearchFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<String> findAllConditions(@PathVariable("patientId") String patientId,
                                          @RequestParam(required = false) String source)
            throws Exception {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        ProblemSearch search = problemSearchFactory.select(sourceType);

        List<Condition> conditions = search.findAllFhirConditions(patientId);
        List<String> output = new ArrayList<>();

        for (Condition condition : conditions) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            new JsonParser().compose(stream, condition);
            output.add(stream.toString("UTF-8"));
        }

        return output;
    }

    @RequestMapping(value = "/{conditionId}", method = RequestMethod.GET)
    public String findCondition(@PathVariable("patientId") String patientId,
                                @PathVariable("conditionId") String conditionId,
                                @RequestParam(required = false) String source)
            throws Exception {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        ProblemSearch search = problemSearchFactory.select(sourceType);

        Condition condition = search.findFhirCondition(patientId, conditionId);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        new JsonParser().compose(stream, condition);
        return stream.toString("UTF-8");
    }
}
