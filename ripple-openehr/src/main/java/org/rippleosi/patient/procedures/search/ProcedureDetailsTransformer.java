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
        procedure.setName(MapUtils.getString(input, "procedure_name"));
        procedure.setNotes(MapUtils.getString(input, "procedure_notes"));
        procedure.setPerformer(MapUtils.getString(input, "performer"));
        procedure.setDate(procedureDate);
        procedure.setTime(procedureTime);
        procedure.setCurrentStatus(MapUtils.getString(input, "status"));
        procedure.setCurrentStatusCode(MapUtils.getString(input, "status_code"));
        procedure.setCurrentStatusTerminology(MapUtils.getString(input, "terminology"));
        procedure.setSource("openehr");

        return procedure;
    }
}
