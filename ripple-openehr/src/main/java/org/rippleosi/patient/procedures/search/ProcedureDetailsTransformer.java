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

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.procedures.model.ProcedureDetails;

/**
 */
public class ProcedureDetailsTransformer implements Transformer<Map<String, Object>, ProcedureDetails> {

    @Override
    public ProcedureDetails transform(Map<String, Object> input) {

        String dateSubmittedAsString = MapUtils.getString(input, "date_submitted");
        String procedureDateAsString = MapUtils.getString(input, "procedure_date");

        Date dateSubmitted = DateFormatter.toDate(dateSubmittedAsString);
        Date procedureDate = DateFormatter.toDateOnly(procedureDateAsString);
        Date procedureTime = DateFormatter.toTimeOnly(procedureDateAsString);

        ProcedureDetails procedure = new ProcedureDetails();
        procedure.setSourceId(MapUtils.getString(input, "uid"));
        procedure.setAuthor(MapUtils.getString(input, "author"));
        procedure.setDateSubmitted(dateSubmitted);
        procedure.setProcedureName(MapUtils.getString(input, "procedure_name"));
        procedure.setNotes(MapUtils.getString(input, "procedure_notes"));
        procedure.setPerformer(MapUtils.getString(input, "performer"));
        procedure.setDate(procedureDate);
        procedure.setTime(procedureTime);
        procedure.setCurrentStatus(MapUtils.getString(input, "status"));
        procedure.setProcedureCode(MapUtils.getString(input, "status_code"));
        procedure.setProcedureTerminology(MapUtils.getString(input, "terminology"));
        procedure.setSource("Marand");

        return procedure;
    }
}
