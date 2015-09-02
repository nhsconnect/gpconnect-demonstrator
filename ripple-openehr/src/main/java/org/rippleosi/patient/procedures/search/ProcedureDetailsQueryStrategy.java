package org.rippleosi.patient.procedures.search;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.exception.InvalidDataException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.procedures.model.ProcedureDetails;

/**
 */
public class ProcedureDetailsQueryStrategy extends AbstractQueryStrategy<ProcedureDetails> {

    private final String procedureId;

    ProcedureDetailsQueryStrategy(String patientId, String procedureId) {
        super(patientId);
        this.procedureId = procedureId;
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_submitted, " +
                "b_a/description[at0001]/items[at0002]/value/value as procedure_name, " +
                "b_a/description[at0001]/items[at0049, 'Procedure notes']/value/value as procedure_notes, " +
                "b_a/other_participations/performer/name as performer, " +
                "b_a/time/value as procedure_date, " +
                "b_a/ism_transition/careflow_step/value as status, " +
                "b_a/ism_transition/careflow_step/defining_code/code_string as status_code, " +
                "b_a/ism_transition/careflow_step/defining_code/terminology_id/value as terminology " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
                "contains ACTION b_a[openEHR-EHR-ACTION.procedure.v1] " +
                "where a/name/value='Procedures list' " +
                "and a/uid/value='" + procedureId + "' ";
    }

    @Override
    public ProcedureDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        if (resultSet.size() > 1) {
            throw new InvalidDataException("Too many results found");
        }

        Map<String,Object> data = resultSet.get(0);

        String dateSubmittedAsString = MapUtils.getString(data, "date_submitted");
        String procedureDateAsString = MapUtils.getString(data, "procedure_date");

        Date dateSubmitted = DateFormatter.toDate(dateSubmittedAsString);
        Date procedureDate = DateFormatter.toDate(procedureDateAsString);

        ProcedureDetails procedure = new ProcedureDetails();
        procedure.setId(MapUtils.getString(data, "uid"));
        procedure.setAuthor(MapUtils.getString(data, "author"));
        procedure.setDateSubmitted(dateSubmitted);
        procedure.setName(MapUtils.getString(data, "procedure_name"));
        procedure.setNotes(MapUtils.getString(data, "procedure_notes"));
        procedure.setPerformer(MapUtils.getString(data, "performer"));
        procedure.setDate(procedureDate);
        procedure.setCurrentStatus(MapUtils.getString(data, "status"));
        procedure.setCurrentStatusCode(MapUtils.getString(data, "status_code"));
        procedure.setCurrentStatusTerminology(MapUtils.getString(data, "terminology"));
        procedure.setSource("openehr");

        return procedure;
    }
}
