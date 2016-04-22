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
package uk.gov.hscic.patient.problems.search;

import java.util.List;

import org.hl7.fhir.instance.model.Condition;
import uk.gov.hscic.common.service.AbstractOpenEhrService;
import uk.gov.hscic.patient.problems.model.ProblemDetails;
import uk.gov.hscic.patient.problems.model.ProblemHeadline;
import uk.gov.hscic.patient.problems.model.ProblemSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.patient.problems.model.ProblemListHTML;

/**
 */
@Service
public class OpenEHRProblemSearch extends AbstractOpenEhrService implements ProblemSearch {

    @Value("${c4hOpenEHR.address}")
    private String openEhrAddress;

    public List<ProblemHeadline> findProblemHeadlines(String patientId) {
        ProblemHeadlineQueryStrategy query = new ProblemHeadlineQueryStrategy(patientId);

        return findData(query);
    }

    public List<ProblemSummary> findAllProblems(String patientId) {
        ProblemSummaryQueryStrategy query = new ProblemSummaryQueryStrategy(patientId);

        return findData(query);
    }

    public ProblemDetails findProblem(String patientId, String problemId) {
        ProblemDetailsQueryStrategy query = new ProblemDetailsQueryStrategy(patientId, problemId);

        return findData(query);
    }

    public List<Condition> findAllFhirConditions(String patientId) {
        String ehrId = new EhrIdLookup().transform(patientId);

        FhirConditionsQueryStrategy query = new FhirConditionsQueryStrategy(patientId, ehrId, openEhrAddress);

        return findData(query);
    }

    public Condition findFhirCondition(String patientId, String conditionId) {
        String ehrId = new EhrIdLookup().transform(patientId);

        FhirConditionQueryStrategy query = new FhirConditionQueryStrategy(patientId, ehrId, openEhrAddress, conditionId);

        return findData(query);
    }

    @Override
    public List<ProblemListHTML> findAllProblemHTMLTables(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ProblemSearch.class);
    }
    
    
}
