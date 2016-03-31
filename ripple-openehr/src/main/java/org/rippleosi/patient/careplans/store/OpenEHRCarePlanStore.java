/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.patient.careplans.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.common.service.CreateStrategy;
import org.rippleosi.common.service.DefaultStoreStrategy;
import org.rippleosi.common.service.UpdateStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.careplans.model.CPRDecision;
import org.rippleosi.patient.careplans.model.CareDocument;
import org.rippleosi.patient.careplans.model.CarePlanDetails;
import org.rippleosi.patient.careplans.model.PrioritiesOfCare;
import org.rippleosi.patient.careplans.model.TreatmentDecision;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRCarePlanStore extends AbstractOpenEhrService implements CarePlanStore {

    @Value("${c4hOpenEHR.endOfLifeTemplate}")
    private String endOfLifeTemplate;

    private static final String CARE_PLAN_PREFIX = "end_of_life_patient_preferences/legal_information:0";

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.CarePlan.Create")
    public void create(String patientId, CarePlanDetails carePlan) {

        Map<String, Object> content = createFlatJsonContent(carePlan);

        CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, endOfLifeTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.CarePlan.Update")
    public void update(String patientId, CarePlanDetails carePlan) {

        Map<String, Object> content = createFlatJsonContent(carePlan);

        UpdateStrategy updateStrategy = new DefaultStoreStrategy(carePlan.getSourceId(), patientId, endOfLifeTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String, Object> createFlatJsonContent(CarePlanDetails carePlan) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        addCareDocument(content, carePlan.getCareDocument());
        addCprDecision(content, carePlan.getCprDecision());
        addTreatmentDecision(content, carePlan.getTreatmentDecision());
        addPriorities(content, carePlan.getPrioritiesOfCare());

        return content;
    }

    private void addCareDocument(Map<String, Object> content, CareDocument careDocument) {
        // TODO
    }

    private void addTreatmentDecision(Map<String, Object> content, TreatmentDecision treatmentDecision) {

        String decisionDate = DateFormatter.toString(treatmentDecision.getDateOfDecision());

        content.put(CARE_PLAN_PREFIX + "/advance_decision_to_refuse_treatment/decision_status|value", treatmentDecision.getDecisionToRefuseTreatment());
        content.put(CARE_PLAN_PREFIX + "/advance_decision_to_refuse_treatment/comment", treatmentDecision.getComment());
        content.put(CARE_PLAN_PREFIX + "/advance_decision_to_refuse_treatment/date_of_decision", decisionDate);
        content.put(CARE_PLAN_PREFIX + "/advance_decision_to_refuse_treatment/decision_status|code", "at0005");
    }

    private void addPriorities(Map<String, Object> content, PrioritiesOfCare priority) {

        String priorityPrefix = CARE_PLAN_PREFIX + "/preferred_priorities_of_care:0";

        content.put(priorityPrefix + "/preferred_place_of_care:0|value", priority.getPlaceOfCare());
        content.put(priorityPrefix + "/preferred_place_of_care:0|code", "at0008");
        content.put(priorityPrefix + "/preferred_place_of_death:0|value", priority.getPlaceOfDeath());
        content.put(priorityPrefix + "/preferred_place_of_death:0|code", "at0018");
        content.put(priorityPrefix + "/comment", priority.getComment());
    }

    private void addCprDecision(Map<String, Object> content, CPRDecision cprDecision) {

        String decisionDate = DateFormatter.toString(cprDecision.getDateOfDecision());

        content.put(CARE_PLAN_PREFIX + "/cpr_decision/cpr_decision|value", cprDecision.getCprDecision());
        content.put(CARE_PLAN_PREFIX + "/cpr_decision/comment", cprDecision.getComment());
        content.put(CARE_PLAN_PREFIX + "/cpr_decision/date_of_cpr_decision", decisionDate);
        content.put(CARE_PLAN_PREFIX + "/cpr_decision/cpr_decision|code", "at0005");
    }
}
