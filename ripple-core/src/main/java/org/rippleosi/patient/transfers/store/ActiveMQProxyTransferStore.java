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
package org.rippleosi.patient.transfers.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.Produce;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.patient.transfers.model.TransferOfCareDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQProxyTransferStore implements TransferOfCareStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyTransferStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Transfers.Create")
    private TransferOfCareStore createTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.ACTIVEMQ;
    }

    @Override
    public void create(@Header("patientId") String patientId, @Body TransferOfCareDetails transferOfCare) {
        try {
            createTopic.create(patientId, transferOfCare);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
