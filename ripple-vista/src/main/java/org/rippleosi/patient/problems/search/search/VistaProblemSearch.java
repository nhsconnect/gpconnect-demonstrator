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
package org.rippleosi.patient.problems.search.search;

import java.util.List;

import org.hl7.fhir.instance.model.Condition;
import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.common.service.AbstractVistaService;
import org.rippleosi.patient.problems.model.ProblemDetails;
import org.rippleosi.patient.problems.model.ProblemHeadline;
import org.rippleosi.patient.problems.model.ProblemSummary;
import org.rippleosi.patient.problems.search.ProblemSearch;
import org.rippleosi.patient.problems.search.model.VistaProblem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VistaProblemSearch extends AbstractVistaService implements ProblemSearch {

    @Value("${vista.address}")
    private String vistaAddress;

    @Override
    public List<ProblemHeadline> findProblemHeadlines(String patientId) {
        VistaProblemToHeadlineRequestStrategy requestStrategy = new VistaProblemToHeadlineRequestStrategy(patientId,
                                                                                                          vistaAddress);

        return findData(requestStrategy, VistaProblem.class);
    }

    @Override
    public List<ProblemSummary> findAllProblems(String patientId) {
        VistaProblemToSummaryRequestStrategy requestStrategy = new VistaProblemToSummaryRequestStrategy(patientId,
                                                                                                        vistaAddress);

        return findData(requestStrategy, VistaProblem.class);
    }

    @Override
    public ProblemDetails findProblem(String patientId, String problemId) {
        VistaProblemToDetailRequestStrategy requestStrategy = new VistaProblemToDetailRequestStrategy(patientId,
                                                                                                      problemId,
                                                                                                      vistaAddress);

        return findData(requestStrategy, VistaProblem.class);
    }

    @Override
    public List<Condition> findAllFhirConditions(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ProblemSearch.class);
    }

    @Override
    public Condition findFhirCondition(String patientId, String conditionId) {
        throw ConfigurationException.unimplementedTransaction(ProblemSearch.class);
    }
}
