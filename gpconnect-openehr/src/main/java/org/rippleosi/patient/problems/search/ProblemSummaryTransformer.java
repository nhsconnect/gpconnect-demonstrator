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

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.problems.model.ProblemSummary;

/**
 */
public class ProblemSummaryTransformer implements Transformer<Map<String, Object>, ProblemSummary> {

    @Override
    public ProblemSummary transform(Map<String, Object> input) {
        Date dateOfOnset = DateFormatter.toDate(MapUtils.getString(input, "onset_date"));
        Date dateTimeOfOnset = DateFormatter.toDate(MapUtils.getString(input, "onset_date_time"));

        ProblemSummary problem = new ProblemSummary();
        problem.setSource("Marand");
        problem.setSourceId(MapUtils.getString(input, "uid"));
        problem.setProblem(MapUtils.getString(input, "problem"));
        problem.setDateOfOnset(dateOfOnset != null ? dateOfOnset : dateTimeOfOnset);

        return problem;
    }
}
