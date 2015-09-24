package org.rippleosi.patient.careplans.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.careplans.model.CPRDecision;
import org.rippleosi.patient.careplans.model.CareDocument;
import org.rippleosi.patient.careplans.model.CarePlanDetails;
import org.rippleosi.patient.careplans.model.PrioritiesOfCare;
import org.rippleosi.patient.careplans.model.TreatmentDecision;

/**
 */
public class CarePlanDetailsTransformer implements Transformer<Map<String, Object>, CarePlanDetails> {

    @Override
    public CarePlanDetails transform(Map<String, Object> input) {

        CarePlanDetails carePlan = new CarePlanDetails();
        carePlan.setSource("openehr");
        carePlan.setSourceId(MapUtils.getString(input, "uid"));
        carePlan.setCareDocument(extractCareDocument(input));
        carePlan.setCprDecision(extractCPRDecision(input));
        carePlan.setPrioritiesOfCare(extractPrioritiesOfCare(input));
        carePlan.setTreatmentDecision(extractTreatmentDecision(input));

        return carePlan;
    }

    private CareDocument extractCareDocument(Map<String, Object> input) {

        Date dateCreated = DateFormatter.toDate(MapUtils.getString(input, "date_created"));

        CareDocument careDocument = new CareDocument();
        careDocument.setName("End of Life Care");
        careDocument.setAuthor(MapUtils.getString(input, "author"));
        careDocument.setDateCreated(dateCreated);
        careDocument.setType("Document");

        return careDocument;
    }

    private CPRDecision extractCPRDecision(Map<String, Object> input) {

        Date dateOfDecision = DateFormatter.toDate(MapUtils.getString(input, "cpr_date_of_decision"));

        CPRDecision cprDecision = new CPRDecision();
        cprDecision.setCprDecision(MapUtils.getString(input, "cpr_decision"));
        cprDecision.setDateOfDecision(dateOfDecision);
        cprDecision.setComment(MapUtils.getString(input, "cpr_comment"));

        return cprDecision;
    }

    private PrioritiesOfCare extractPrioritiesOfCare(Map<String, Object> input) {

        PrioritiesOfCare prioritiesOfCare = new PrioritiesOfCare();
        prioritiesOfCare.setPlaceOfCare(MapUtils.getString(input, "priority_place_of_care"));
        prioritiesOfCare.setPlaceOfDeath(MapUtils.getString(input, "priority_place_of_death"));
        prioritiesOfCare.setComment(MapUtils.getString(input, "priority_comment"));

        return prioritiesOfCare;
    }

    private TreatmentDecision extractTreatmentDecision(Map<String, Object> input) {

        Date dateOfDecision = DateFormatter.toDate(MapUtils.getString(input, "treatment_date_of_decision"));

        TreatmentDecision treatmentDecision = new TreatmentDecision();
        treatmentDecision.setDecisionToRefuseTreatment(MapUtils.getString(input, "treatment_decision"));
        treatmentDecision.setDateOfDecision(dateOfDecision);
        treatmentDecision.setComment(MapUtils.getString(input, "treatment_comment"));

        return treatmentDecision;
    }
}
