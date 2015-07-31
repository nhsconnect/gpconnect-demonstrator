package net.nhs.esb.endoflife.route.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.nhs.esb.endoflife.model.CareDocument;
import net.nhs.esb.endoflife.model.CprDecision;
import net.nhs.esb.endoflife.model.EndOfLifeCarePlan;
import net.nhs.esb.endoflife.model.EndOfLifeComposition;
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
}
