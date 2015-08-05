package net.nhs.esb.endoflife.route.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nhs.esb.endoflife.model.CareDocument;
import net.nhs.esb.endoflife.model.CprDecision;
import net.nhs.esb.endoflife.model.EndOfLifeCarePlan;
import net.nhs.esb.endoflife.model.EndOfLifeUpdate;
import net.nhs.esb.endoflife.model.PrioritiesOfCare;
import net.nhs.esb.endoflife.model.TreatmentDecision;
import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.util.DateFormatter;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class EndOfLifeCompositionConverter {

    private static final String CARE_PLAN_PREFIX = "end_of_life_patient_preferences/legal_information:0";

    @Converter
    public EndOfLifeCarePlan convertResponseToEndOfLifeComposition(CompositionResponseData responseData) {

        Map<String, Object> rawComposition = responseData.getComposition();

        return extractCarePlan(rawComposition);
    }

    @Converter
    public EndOfLifeUpdate convertEndOfLifeCompositionToUpdate(EndOfLifeCarePlan endOfLifeCarePlan) {

        Map<String, String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        addCareDocument(content, CARE_PLAN_PREFIX, endOfLifeCarePlan.getCareDocument());
        addCprDecision(content, CARE_PLAN_PREFIX, endOfLifeCarePlan.getCprDecision());
        addTreatmentDecision(content, CARE_PLAN_PREFIX, endOfLifeCarePlan.getTreatmentDecision());
        addPriorities(content, CARE_PLAN_PREFIX, endOfLifeCarePlan.getPrioritiesOfCare());

        return new EndOfLifeUpdate(content);
    }

    private int countEntries(Map<String, Object> rawComposition, String prefix) {

        int count = 0;
        for (String key : rawComposition.keySet()) {
            if (key.startsWith(prefix)) {
                int index = Integer.parseInt(StringUtils.substringBefore(StringUtils.substringAfter(key, prefix), "/"));
                count = Math.max(count, index + 1);
            }
        }

        return count;
    }

    private EndOfLifeCarePlan extractCarePlan(Map<String, Object> rawComposition) {

        String compositionId = MapUtils.getString(rawComposition, "end_of_life_patient_preferences/_uid");

        EndOfLifeCarePlan carePlan = new EndOfLifeCarePlan();
        carePlan.setCompositionId(compositionId);
        carePlan.setCprDecision(extractCprDecision(rawComposition, CARE_PLAN_PREFIX));
        carePlan.setTreatmentDecision(extractTreatmentDecision(rawComposition, CARE_PLAN_PREFIX));
        carePlan.setCareDocument(extractCareDocument(rawComposition));
        carePlan.setSource("openehr");

        List<PrioritiesOfCare> prioritiesOfCareList = extractPrioritiesOfCare(rawComposition, CARE_PLAN_PREFIX);
        if (!prioritiesOfCareList.isEmpty()) {
            carePlan.setPrioritiesOfCare(prioritiesOfCareList.get(0));
        }

        return carePlan;
    }

    private CprDecision extractCprDecision(Map<String, Object> rawComposition, String prefix) {

        String dateTime = MapUtils.getString(rawComposition, prefix + "/cpr_decision/date_of_cpr_decision");

        CprDecision cprDecision = new CprDecision();
        cprDecision.setCprDecision(MapUtils.getString(rawComposition, prefix + "/cpr_decision/cpr_decision|value"));
        cprDecision.setComment(MapUtils.getString(rawComposition, prefix + "/cpr_decision/comment"));
        cprDecision.setDateOfDecision(DateFormatter.toDate(dateTime));

        return cprDecision;
    }

    private TreatmentDecision extractTreatmentDecision(Map<String, Object> rawComposition, String prefix) {

        String dateTime = MapUtils.getString(rawComposition, prefix + "/advance_decision_to_refuse_treatment/date_of_decision");

        TreatmentDecision treatmentDecision = new TreatmentDecision();
        treatmentDecision.setDecisionToRefuseTreatment(MapUtils.getString(rawComposition, prefix + "/advance_decision_to_refuse_treatment/decision_status|value"));
        treatmentDecision.setComment(MapUtils.getString(rawComposition, prefix + "/advance_decision_to_refuse_treatment/comment"));
        treatmentDecision.setDateOfDecision(DateFormatter.toDate(dateTime));

        return treatmentDecision;
    }

    private CareDocument extractCareDocument(Map<String, Object> rawComposition) {

        String dateTime = MapUtils.getString(rawComposition, "end_of_life_patient_preferences/context/start_time");

        // TODO: Remove hard-coded data
        CareDocument careDocument = new CareDocument();
        careDocument.setName("End of Life Care");
        careDocument.setType("Document");
        careDocument.setAuthor("Dr John Smith");
        careDocument.setDate(DateFormatter.toDate(dateTime));

        return careDocument;
    }

    private List<PrioritiesOfCare> extractPrioritiesOfCare(Map<String, Object> rawComposition, String prefix) {

        String priorityPrefix = prefix + "/preferred_priorities_of_care:";
        int count = countEntries(rawComposition, priorityPrefix);

        List<PrioritiesOfCare> prioritiesOfCareList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            PrioritiesOfCare prioritiesOfCare = extractPrioritiesOfCare(rawComposition, priorityPrefix, i);
            prioritiesOfCareList.add(prioritiesOfCare);
        }

        return prioritiesOfCareList;
    }

    private PrioritiesOfCare extractPrioritiesOfCare(Map<String, Object> rawComposition, String prefix, int index) {

        PrioritiesOfCare prioritiesOfCare = new PrioritiesOfCare();
        prioritiesOfCare.setPlaceOfCare(MapUtils.getString(rawComposition, prefix + index + "/preferred_place_of_care:0|value"));
        prioritiesOfCare.setPlaceOfDeath(MapUtils.getString(rawComposition, prefix + index + "/preferred_place_of_death:0|value"));
        prioritiesOfCare.setComment(MapUtils.getString(rawComposition, prefix + index + "/comment"));

        return prioritiesOfCare;
    }


    private void addCareDocument(Map<String, String> content, String prefix, CareDocument careDocument) {
        // TODO
    }

    private void addTreatmentDecision(Map<String, String> content, String prefix, TreatmentDecision treatmentDecision) {

        String decisionDate = DateFormatter.toString(treatmentDecision.getDateOfDecision());

        content.put(prefix + "/advance_decision_to_refuse_treatment/decision_status|value", treatmentDecision.getDecisionToRefuseTreatment());
        content.put(prefix + "/advance_decision_to_refuse_treatment/comment", treatmentDecision.getComment());
        content.put(prefix + "/advance_decision_to_refuse_treatment/date_of_decision", decisionDate);
        content.put(prefix + "/advance_decision_to_refuse_treatment/decision_status|code", "at0005");
    }

    private void addPriorities(Map<String, String> content, String prefix, PrioritiesOfCare priority) {

        String priorityPrefix = prefix + "/preferred_priorities_of_care:0";

        content.put(priorityPrefix + "/preferred_place_of_care:0|value", priority.getPlaceOfCare());
        content.put(priorityPrefix + "/preferred_place_of_care:0|code", "at0008");
        content.put(priorityPrefix + "/preferred_place_of_death:0|value", priority.getPlaceOfDeath());
        content.put(priorityPrefix + "/preferred_place_of_death:0|code", "at0018");
        content.put(priorityPrefix + "/comment", priority.getComment());
    }

    private void addCprDecision(Map<String, String> content, String prefix, CprDecision cprDecision) {

        String decisionDate = DateFormatter.toString(cprDecision.getDateOfDecision());

        content.put(prefix + "/cpr_decision/cpr_decision|value", cprDecision.getCprDecision());
        content.put(prefix + "/cpr_decision/comment", cprDecision.getComment());
        content.put(prefix + "/cpr_decision/date_of_cpr_decision", decisionDate);
        content.put(prefix + "/cpr_decision/cpr_decision|code", "at0005");
    }
}
