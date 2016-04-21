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
package org.rippleosi.patient.procedures.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.common.service.CreateStrategy;
import org.rippleosi.common.service.DefaultStoreStrategy;
import org.rippleosi.common.service.UpdateStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.procedures.model.ProcedureDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRProcedureStore extends AbstractOpenEhrService implements ProcedureStore {

    @Value("${c4hOpenEHR.proceduresTemplate}")
    private String proceduresTemplate;

    private static final String PROCEDURE_PREFIX = "procedures_list/procedures:0/procedure:0";

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.Procedures.Create")
    public void create(String patientId, ProcedureDetails procedure) {

        Map<String,Object> content = createFlatJsonContent(procedure);

        CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, proceduresTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.Procedures.Update")
    public void update(String patientId, ProcedureDetails procedure) {

        Map<String,Object> content = createFlatJsonContent(procedure);

        UpdateStrategy updateStrategy = new DefaultStoreStrategy(procedure.getSourceId(), patientId, proceduresTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String,Object> createFlatJsonContent(ProcedureDetails procedure) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        String dateTime = DateFormatter.combineDateTime(procedure.getDate(), procedure.getTime());

        content.put(PROCEDURE_PREFIX + "/procedure_name", procedure.getProcedureName());
        content.put(PROCEDURE_PREFIX + "/procedure_notes", procedure.getNotes());
        content.put(PROCEDURE_PREFIX + "/ism_transition/careflow_step|code", "at0047");
        content.put(PROCEDURE_PREFIX + "/ism_transition/careflow_step|terminology", "local");
        content.put(PROCEDURE_PREFIX + "/_other_participation:0|function", "Performer");
        content.put(PROCEDURE_PREFIX + "/_other_participation:0|name", procedure.getPerformer());
        content.put(PROCEDURE_PREFIX + "/time", dateTime);

        return content;
    }
}
