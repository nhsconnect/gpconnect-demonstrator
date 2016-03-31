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
package org.rippleosi.patient.mdtreports.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.common.service.CreateStrategy;
import org.rippleosi.common.service.DefaultStoreStrategy;
import org.rippleosi.common.service.UpdateStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.mdtreports.model.MDTReportDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRMDTReportStore extends AbstractOpenEhrService implements MDTReportStore {

    @Value("${c4hOpenEHR.mdtReportTemplate}")
    private String mdtReportTemplate;

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.MDTReport.Create")
    public void create(String patientId, MDTReportDetails mdtReport) {

        Map<String, Object> content = createFlatJsonContent(mdtReport);

        CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, mdtReportTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.MDTReport.Update")
    public void update(String patientId, MDTReportDetails mdtReport) {

        Map<String, Object> content = createFlatJsonContent(mdtReport);

        UpdateStrategy updateStrategy = new DefaultStoreStrategy(mdtReport.getSourceId(), patientId, mdtReportTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String, Object> createFlatJsonContent(MDTReportDetails mdtReport) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        String meetingTime = DateFormatter.combineDateTime(mdtReport.getDateOfMeeting(), mdtReport.getTimeOfMeeting());

        content.put("mdt_output_report/history/question_to_mdt/question_to_mdt", mdtReport.getQuestion());
        content.put("mdt_output_report/plan_and_requested_actions/recommendation/meeting_notes", mdtReport.getNotes());
        content.put("mdt_output_report/context/start_time", DateFormatter.toString(mdtReport.getDateOfRequest()));

        content.put("mdt_output_report/referral_details/mdt_referral/narrative", "MDT Referral");
        content.put("mdt_output_report/referral_details/mdt_referral/mdt_team", mdtReport.getServiceTeam());
        content.put("mdt_output_report/referral_details/mdt_referral/request:0/service_requested", "MDT referral");
        content.put("mdt_output_report/referral_details/mdt_referral/request:0/timing", meetingTime);
        content.put("mdt_output_report/referral_details/mdt_referral/request:0/timing|formalism", "timing");
        content.put("mdt_output_report/referral_details/referral_tracking/referred_service", "MDT referral");
        content.put("mdt_output_report/referral_details/referral_tracking/ism_transition/current_state|code", "526");
        content.put("mdt_output_report/referral_details/referral_tracking/ism_transition/current_state|value", "planned");
        content.put("mdt_output_report/referral_details/referral_tracking/ism_transition/careflow_step|code", "at0002");
        content.put("mdt_output_report/referral_details/referral_tracking/ism_transition/careflow_step|value", "Referral planned");

        return content;
    }
}
