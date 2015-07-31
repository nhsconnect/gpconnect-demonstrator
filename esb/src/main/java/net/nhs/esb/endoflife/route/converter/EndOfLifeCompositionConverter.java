package net.nhs.esb.endoflife.route.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nhs.esb.endoflife.model.CareDocument;
import net.nhs.esb.endoflife.model.CprDecision;
import net.nhs.esb.endoflife.model.EndOfLifeCarePlan;
import net.nhs.esb.endoflife.model.EndOfLifeComposition;
import net.nhs.esb.endoflife.model.EndOfLifeUpdate;
import net.nhs.esb.endoflife.model.PrioritiesOfCare;
import net.nhs.esb.endoflife.model.TreatmentDecision;
import net.nhs.esb.openehr.model.CompositionResponseData;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class EndOfLifeCompositionConverter {

    private static final String CARE_PLAN_PREFIX = "end_of_life_patient_preferences/legal_information:";

    @Converter
    public EndOfLifeComposition convertResponseToEndOfLifeComposition(CompositionResponseData responseData) {

        Map<String, Object> rawComposition = responseData.getComposition();

        String compositionId = MapUtils.getString(rawComposition, "end_of_life_patient_preferences/_uid");

        List<EndOfLifeCarePlan> carePlanList = new ArrayList<>();

        int count = countEntries(rawComposition, CARE_PLAN_PREFIX);
        for (int i = 0; i < count; i++) {
            EndOfLifeCarePlan carePlan = extractCarePlan(rawComposition, i);
            carePlanList.add(carePlan);
        }

        EndOfLifeComposition endOfLifeComposition = new EndOfLifeComposition();
        endOfLifeComposition.setCompositionId(compositionId);
        endOfLifeComposition.setEolCarePlans(carePlanList);

        return endOfLifeComposition;
    }

    @Converter
    public EndOfLifeUpdate convertEndOfLifeCompositionToUpdate(EndOfLifeComposition endOfLifeComposition) {

        Map<String,String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        int index = 0;
        for (EndOfLifeCarePlan endOfLifeCarePlan : endOfLifeComposition.getEolCarePlans()) {

            String prefix = CARE_PLAN_PREFIX + index;

            addCareDocument(content, prefix, endOfLifeCarePlan.getCareDocument());
            addCprDecision(content, prefix, endOfLifeCarePlan.getCprDecision());
            addTreatmentDecision(content, prefix, endOfLifeCarePlan.getTreatmentDecision());
            addPriorities(content, prefix, endOfLifeCarePlan.getPrioritiesOfCare());

            index++;
        }

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

    private EndOfLifeCarePlan extractCarePlan(Map<String, Object> rawComposition, int index) {

        String prefix = CARE_PLAN_PREFIX + index;

        EndOfLifeCarePlan carePlan = new EndOfLifeCarePlan();
        carePlan.setCprDecision(extractCprDecision(rawComposition, prefix));
        carePlan.setTreatmentDecision(extractTreatmentDecision(rawComposition, prefix));
        carePlan.setCareDocument(extractCareDocument());
        carePlan.setPrioritiesOfCare(extractPrioritiesOfCare(rawComposition, prefix));
        carePlan.setSource("openehr");

        return carePlan;
    }

    private CprDecision extractCprDecision(Map<String, Object> rawComposition, String prefix) {

        String dateTime = MapUtils.getString(rawComposition, prefix + "/cpr_decision/date_of_cpr_decision");
        String decisionDate = StringUtils.substringBefore(dateTime, "T");

        CprDecision cprDecision = new CprDecision();
        cprDecision.setCprDecision(MapUtils.getString(rawComposition, prefix + "/cpr_decision/cpr_decision|value"));
        cprDecision.setComment(MapUtils.getString(rawComposition, prefix + "/cpr_decision/comment"));
        cprDecision.setDateOfDecision(decisionDate);

        return cprDecision;
    }

    private TreatmentDecision extractTreatmentDecision(Map<String, Object> rawComposition, String prefix) {

        String dateTime = MapUtils.getString(rawComposition, prefix + "/advance_decision_to_refuse_treatment/date_of_decision");
        String decisionDate = StringUtils.substringBefore(dateTime, "T");

        TreatmentDecision treatmentDecision = new TreatmentDecision();
        treatmentDecision.setDecisionToRefuseTreatment(MapUtils.getString(rawComposition, prefix + "/advance_decision_to_refuse_treatment/decision_status|value"));
        treatmentDecision.setComment(MapUtils.getString(rawComposition, prefix + "/advance_decision_to_refuse_treatment/comment"));
        treatmentDecision.setDateOfDecision(decisionDate);

        return treatmentDecision;
    }

    private CareDocument extractCareDocument() {
        // TODO: Remove hard-coded data
        CareDocument careDocument = new CareDocument();
        careDocument.setName("End of Life Care");
        careDocument.setType("Document");
        careDocument.setAuthor("Dr John Smith");
        careDocument.setDate("2015-01-01");

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

        String decisionDate = treatmentDecision.getDateOfDecision() + "T00:00:00Z";

        content.put(prefix + "/advance_decision_to_refuse_treatment/decision_status|value", treatmentDecision.getDecisionToRefuseTreatment());
        content.put(prefix + "/advance_decision_to_refuse_treatment/comment", treatmentDecision.getComment());
        content.put(prefix + "/advance_decision_to_refuse_treatment/date_of_decision", decisionDate);
        content.put(prefix + "/advance_decision_to_refuse_treatment/decision_status|code", "at0005");
    }

    private void addPriorities(Map<String, String> content, String prefix, List<PrioritiesOfCare> prioritiesOfCare) {

        int index = 0;
        for (PrioritiesOfCare priority : prioritiesOfCare) {
            String priorityPrefix = prefix + "/preferred_priorities_of_care:" + index;

            content.put(priorityPrefix + "/preferred_place_of_care:0|value", priority.getPlaceOfCare());
            content.put(priorityPrefix + "/preferred_place_of_care:0|code", "at0008");
            content.put(priorityPrefix + "/preferred_place_of_death:0|value", priority.getPlaceOfDeath());
            content.put(priorityPrefix + "/preferred_place_of_death:0|code", "at0018");
            content.put(priorityPrefix + "/comment", priority.getComment());

            index++;
        }
    }

    private void addCprDecision(Map<String, String> content, String prefix, CprDecision cprDecision) {

        String decisionDate = cprDecision.getDateOfDecision() + "T00:00:00Z";

        content.put(prefix + "/cpr_decision/cpr_decision|value", cprDecision.getCprDecision());
        content.put(prefix + "/cpr_decision/comment", cprDecision.getComment());
        content.put(prefix + "/cpr_decision/date_of_cpr_decision", decisionDate);
        content.put(prefix + "/cpr_decision/cpr_decision|code", "at0005");
    }
}
