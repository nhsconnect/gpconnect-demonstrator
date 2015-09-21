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
        procedure.setSource("openehr");
        procedure.setSourceId(MapUtils.getString(input, "uid"));
        procedure.setName(MapUtils.getString(input, "procedure_name"));
        procedure.setDate(date);
        procedure.setTime(time);

        return procedure;
    }
}
