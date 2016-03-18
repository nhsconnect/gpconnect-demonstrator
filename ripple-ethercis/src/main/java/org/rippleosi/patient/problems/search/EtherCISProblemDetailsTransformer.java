/*
 *  Copyright 2015 Ripple OSI
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
 *
 */
package org.rippleosi.patient.problems.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.problems.model.ProblemDetails;

/**
 */
public class EtherCISProblemDetailsTransformer implements Transformer<Map<String, Object>, ProblemDetails> {

    @Override
    public ProblemDetails transform(Map<String, Object> input) {

        ProblemDetails problem = new ProblemDetails();
        problem.setSource("EtherCIS");
        problem.setSourceId(MapUtils.getString(input, "uid"));
        problem.setProblem(MapUtils.getString(input, "problem"));
        problem.setCode(MapUtils.getString(input, "problem_code"));
        problem.setTerminology(MapUtils.getString(input, "problem_terminology"));
        problem.setDescription(MapUtils.getString(input, "description"));
        problem.setAuthor(MapUtils.getString(input, "author"));

        String onsetDate = MapUtils.getString(input, "onset_date");
        problem.setDateOfOnset(DateFormatter.toDate(onsetDate));

        String dateCreated = MapUtils.getString(input, "date_created");
        problem.setDateCreated(DateFormatter.toDate(dateCreated));

        return problem;
    }
}
