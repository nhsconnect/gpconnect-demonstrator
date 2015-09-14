package org.rippleosi.patient.procedures.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.procedures.model.ProcedureSummary;

/**
 */
public class ProcedureSummaryQueryStrategy extends AbstractQueryStrategy<List<ProcedureSummary>> {

    ProcedureSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "b_a/description[at0001]/items[at0002]/value/value as procedure_name, " +
                "b_a/time/value as procedure_date " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
                "contains ACTION b_a[openEHR-EHR-ACTION.procedure.v1] " +
                "where a/name/value='Procedures list' ";
    }

    @Override
    public List<ProcedureSummary> transform(List<Map<String, Object>> resultSet) {

        List<ProcedureSummary> summaries = new ArrayList<>();

        for (Map<String, Object> data : resultSet) {
            ProcedureSummary procedure = new ProcedureSummary();

            String uid = MapUtils.getString(data, "uid");
            String name = MapUtils.getString(data, "procedure_name");
            String dateAsString = MapUtils.getString(data, "procedure_date");

            Date date = DateFormatter.toDateOnly(dateAsString);
            Date time = DateFormatter.toTimeOnly(dateAsString);

            procedure.setSourceId(uid);
            procedure.setName(name);
            procedure.setDate(date);
            procedure.setTime(time);
            procedure.setSource("openehr");

            summaries.add(procedure);
        }

        return summaries;
    }
}
