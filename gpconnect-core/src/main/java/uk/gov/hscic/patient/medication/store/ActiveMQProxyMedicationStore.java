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
package uk.gov.hscic.patient.medication.store;

import org.apache.camel.Produce;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.medication.model.MedicationDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class ActiveMQProxyMedicationStore implements MedicationStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyMedicationStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Medication.Create")
    private MedicationStore createTopic;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Medication.Update")
    private MedicationStore updateTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.ACTIVEMQ;
    }

    @Override
    public void create(String patientId, MedicationDetails medication) {
        try {
            createTopic.create(patientId, medication);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void update(String patientId, MedicationDetails medication) {
        try {
            updateTopic.update(patientId, medication);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
