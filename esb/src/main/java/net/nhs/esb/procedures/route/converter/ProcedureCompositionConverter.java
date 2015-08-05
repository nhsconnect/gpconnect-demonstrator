package net.nhs.esb.procedures.route.converter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.procedures.model.Procedure;
import net.nhs.esb.procedures.model.ProcedureUpdate;
import net.nhs.esb.util.DateFormatter;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class ProcedureCompositionConverter {

    private static final String PROCEDURE_PREFIX = "procedures_list/procedures:0/procedure:0";

    @Converter
    public Procedure convertResponseToProcedureComposition(CompositionResponseData responseData) {

        Map<String, Object> rawComposition = responseData.getComposition();

        return extractProcedure(rawComposition);
    }

    @Converter
    public ProcedureUpdate convertProcedureCompositionToUpdate(Procedure procedure) {

        Map<String, String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");
        content.put("ctx/id_scheme", "NHS");
        content.put("ctx/id_namespace", "NHS");

        String dateTime = DateFormatter.combineDateTime(procedure.getDateOfProcedure(), procedure.getTimeOfProcedure());

        content.put(PROCEDURE_PREFIX + "/procedure_name", procedure.getProcedureName());
        content.put(PROCEDURE_PREFIX + "/procedure_notes", procedure.getProcedureNotes());
        content.put(PROCEDURE_PREFIX + "/ism_transition/careflow_step|code", procedure.getCode());
        content.put(PROCEDURE_PREFIX + "/ism_transition/careflow_step|terminology", procedure.getTerminology());
        content.put(PROCEDURE_PREFIX + "/_other_participation:0|function", "Performer");
        content.put(PROCEDURE_PREFIX + "/_other_participation:0|name", procedure.getPerformer());
        content.put(PROCEDURE_PREFIX + "/time", dateTime);

        return new ProcedureUpdate(content);
    }

    private int countProcedures(Map<String, Object> rawComposition) {

        int index = -1;
        boolean found;

        do {
            index++;
            found = rawComposition.containsKey(PROCEDURE_PREFIX + index + "/procedure_name");
        }
        while (found);

        return index;
    }

    private Procedure extractProcedure(Map<String, Object> rawComposition) {

        String compositionId = MapUtils.getString(rawComposition, "procedures_list/_uid");

        String dateSubmitted = MapUtils.getString(rawComposition, "procedures_list/context/start_time");
        String procedureName = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + "/procedure_name");
        String procedureNotes = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + "/procedure_notes");
        String performer = findPerformer(rawComposition);

        String code = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + "/ism_transition/careflow_step|code");
        String terminology = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + "/ism_transition/careflow_step|terminology");

        String dateTime = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + "/time");
        Date date = DateFormatter.toDateOnly(dateTime);
        Date time = DateFormatter.toTimeOnly(dateTime);

        Procedure procedure = new Procedure();
        procedure.setCompositionId(compositionId);
        procedure.setDateSubmitted(DateFormatter.toDate(dateSubmitted));
        procedure.setDateOfProcedure(date);
        procedure.setTimeOfProcedure(time);
        procedure.setProcedureName(procedureName);
        procedure.setProcedureNotes(procedureNotes);
        procedure.setPerformer(performer);
        procedure.setCode(code);
        procedure.setTerminology(terminology);
        procedure.setSource("openehr");

        return procedure;
    }

    private String findPerformer(Map<String, Object> rawComposition) {

        String prefix = PROCEDURE_PREFIX + "/_other_participation:";
        String role;

        int index = 0;
        do {
            role = MapUtils.getString(rawComposition, prefix + index + "|function");

            if (StringUtils.equals(role, "Performer")) {
                return MapUtils.getString(rawComposition, prefix + index + "|name");
            }

            index++;
        }
        while (role != null);

        return null;
    }
}
