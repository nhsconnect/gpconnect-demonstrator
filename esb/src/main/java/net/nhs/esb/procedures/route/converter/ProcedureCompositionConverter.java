package net.nhs.esb.procedures.route.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.procedures.model.Procedure;
import net.nhs.esb.procedures.model.ProcedureComposition;
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

    private int countProcedures(Map<String,Object> rawComposition) {

        int index = -1;
        boolean found = true;

        while (found) {
            index++;
            found = rawComposition.containsKey(PROCEDURE_PREFIX + index + "/procedure_name");
        }

        return index;
    }

    private Procedure extractProcedure(Map<String,Object> rawComposition, int index) {

        String procedureName = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + index + "/procedure_name");
        String procedureNotes = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + index + "/procedure_notes");

        String dateTime = MapUtils.getString(rawComposition, PROCEDURE_PREFIX + index + "/time");
        String date = StringUtils.substringBefore(dateTime, "T");
        String time = StringUtils.substringBeforeLast(StringUtils.substringAfter(dateTime, "T"), ":");

        Procedure procedure = new Procedure();
        procedure.setDateOfProcedure(date);
        procedure.setProcedureName(procedureName);
        procedure.setProcedureNotes(procedureNotes);
        procedure.setTimeOfProcedure(time);

        return procedure;
    }
}
