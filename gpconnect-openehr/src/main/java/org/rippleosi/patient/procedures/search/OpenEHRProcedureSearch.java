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
package org.rippleosi.patient.procedures.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.procedures.model.ProcedureDetails;
import org.rippleosi.patient.procedures.model.ProcedureSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRProcedureSearch extends AbstractOpenEhrService implements ProcedureSearch {

    @Override
    public List<ProcedureSummary> findAllProcedures(String patientId) {
        ProcedureSummaryQueryStrategy query = new ProcedureSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public ProcedureDetails findProcedure(String patientId, String procedureId) {
        ProcedureDetailsQueryStrategy query = new ProcedureDetailsQueryStrategy(patientId, procedureId);

        return findData(query);
    }
}
