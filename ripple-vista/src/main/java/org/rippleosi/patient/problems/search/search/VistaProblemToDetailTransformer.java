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
import org.rippleosi.patient.problems.model.ProblemDetails;
import org.rippleosi.patient.problems.search.model.VistaProblem;

public class VistaProblemToDetailTransformer implements Transformer<VistaProblem, ProblemDetails> {

    @Override
    public ProblemDetails transform(VistaProblem input) {
        ProblemDetails details = new ProblemDetails();

        details.setSource("vista");
        details.setSourceId(input.getUid());
        details.setProblem(input.getIcdName());
        details.setDateOfOnset(input.getOnset());
        details.setDescription(input.getProblemText());
        details.setTerminology(input.getIcdName());
        details.setCode(input.getIcdCode());
        details.setAuthor(input.getProviderName());
        details.setDateCreated(input.getEntered());

        return details;
    }
}
