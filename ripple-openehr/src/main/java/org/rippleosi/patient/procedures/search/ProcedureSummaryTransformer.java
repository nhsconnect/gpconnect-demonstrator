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
import org.rippleosi.patient.procedures.model.ProcedureSummary;

/**
 */
public class ProcedureSummaryTransformer implements Transformer<Map<String, Object>, ProcedureSummary> {

    @Override
    public ProcedureSummary transform(Map<String, Object> input) {

        String dateAsString = MapUtils.getString(input, "procedure_date");

        Date date = DateFormatter.toDateOnly(dateAsString);
        Date time = DateFormatter.toTimeOnly(dateAsString);

        ProcedureSummary procedure = new ProcedureSummary();
        procedure.setSource("Marand");
        procedure.setSourceId(MapUtils.getString(input, "uid"));
        procedure.setName(MapUtils.getString(input, "procedure_name"));
        procedure.setDate(date);
        procedure.setTime(time);

        return procedure;
    }
}
