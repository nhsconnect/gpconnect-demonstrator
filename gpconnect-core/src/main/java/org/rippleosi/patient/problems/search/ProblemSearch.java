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
package org.rippleosi.patient.problems.search;

import java.util.List;

import org.hl7.fhir.instance.model.Condition;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.problems.model.ProblemDetails;
import org.rippleosi.patient.problems.model.ProblemHeadline;
import org.rippleosi.patient.problems.model.ProblemSummary;

/**
 */
public interface ProblemSearch extends Repository {

    List<ProblemHeadline> findProblemHeadlines(String patientId);

    List<ProblemSummary> findAllProblems(String patientId);

    ProblemDetails findProblem(String patientId, String problemId);

    List<Condition> findAllFhirConditions(String patientId);

    Condition findFhirCondition(String patientId, String conditionId);
}
