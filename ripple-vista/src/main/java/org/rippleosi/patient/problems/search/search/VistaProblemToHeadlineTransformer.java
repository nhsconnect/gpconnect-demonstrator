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

import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.problems.search.model.VistaProblem;
import org.rippleosi.patient.problems.model.ProblemHeadline;

public class VistaProblemToHeadlineTransformer implements Transformer<VistaProblem, ProblemHeadline> {

    @Override
    public ProblemHeadline transform(VistaProblem input) {
        ProblemHeadline headline = new ProblemHeadline();

        headline.setSource("vista");
        headline.setSourceId(input.getUid());
        headline.setProblem(input.getIcdName());

        return headline;
    }
}
