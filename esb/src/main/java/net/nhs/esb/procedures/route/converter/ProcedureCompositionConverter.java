package net.nhs.esb.procedures.route.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.procedures.model.Procedure;
import net.nhs.esb.procedures.model.ProcedureComposition;
import net.nhs.esb.procedures.model.ProcedureUpdate;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class ProcedureCompositionConverter {

    private static final String PROCEDURE_PREFIX = "procedures_list/procedures:0/procedure:";

    @Converter
    public ProcedureComposition convertResponseToProcedureComposition(CompositionResponseData responseData) {

        Map<String, Object> rawComposition = responseData.getComposition();

        String compositionId = MapUtils.getString(rawComposition, "procedures_list/_uid");

        List<Procedure> procedureList = new ArrayList<>();

        int count = countProcedures(rawComposition);
        for (int i = 0; i < count; i++) {
            Procedure procedure = extractProcedure(rawComposition, i);
            procedureList.add(procedure);
        }

        ProcedureComposition procedureComposition = new ProcedureComposition();
        procedureComposition.setCompositionId(compositionId);
        procedureComposition.setProcedures(procedureList);

        return procedureComposition;
    }

    @Converter
    public ProcedureUpdate convertProcedureCompositionToUpdate(ProcedureComposition procedureComposition) {

        Map<String,String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");
        content.put("ctx/id_scheme", "NHS");
        content.put("ctx/id_namespace", "NHS");

        int index = 0;
        for (Procedure procedure : procedureComposition.getProcedures()) {

            String dateTime = procedure.getDateOfProcedure() + "T" + procedure.getTimeOfProcedure() + ":00";

            content.put(PROCEDURE_PREFIX + index + "/procedure_name", procedure.getProcedureName());
            content.put(PROCEDURE_PREFIX + index + "/procedure_notes", procedure.getProcedureNotes());
            content.put(PROCEDURE_PREFIX + index + "/ism_transition/careflow_step|code", procedure.getCode());
            content.put(PROCEDURE_PREFIX + index + "/ism_transition/careflow_step|terminology", procedure.getTerminology());
            content.put(PROCEDURE_PREFIX + index + "/_other_participation:0|function", "Performer");
            content.put(PROCEDURE_PREFIX + index + "/_other_participation:0|name", procedure.getPerformer());
            content.put(PROCEDURE_PREFIX + index + "/time", dateTime);

            index++;
        }

        return new ProcedureUpdate(content);
    }

    private int countProcedures(Map<String,Object> rawComposition) {

        int index = -1;
        boolean found;

        do {
            index++;
            found = rawComposition.containsKey(PROCEDURE_PREFIX + index + "/procedure_name");
        }
        while (found);

        return index;
    }

    private Procedure extractProcedure(Map<String,Object> rawComposition, int index) {

        String procedureName = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + index + "/procedure_name");
        String procedureNotes = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + index + "/procedure_notes");
        String performer = findPerformer(rawComposition, index);

        String code = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + index + "/ism_transition/careflow_step|code");
        String terminology = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + index + "/ism_transition/careflow_step|terminology");

        String dateTime = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + index + "/time");
        String date = StringUtils.substringBefore(dateTime, "T");
        String time = StringUtils.substringBeforeLast(StringUtils.substringAfter(dateTime, "T"), ":");

        Procedure procedure = new Procedure();
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

    private String findPerformer(Map<String,Object> rawComposition, int procedureIndex) {

        String prefix = PROCEDURE_PREFIX + procedureIndex + "/_other_participation:";
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
