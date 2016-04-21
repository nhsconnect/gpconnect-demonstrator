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
package uk.gov.hscic.patient.procedures.store;

import org.apache.camel.Consume;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.procedures.model.ProcedureDetails;
import uk.gov.hscic.patient.procedures.store.ProcedureStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class AuditLogFileProcedureStore implements ProcedureStore {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogFileProcedureStore.class);

    @Value("${repository.config.audit:2000}")
    private int priority;

    @Override
    public RepoSource getSource() {
        return RepoSourceType.AUDIT;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    @Consume(uri = "activemq:Consumer.Audit.VirtualTopic.Ripple.Procedures.Create")
    public void create(String patientId, ProcedureDetails procedure) {
        logger.info("AUDIT: Received procedure create request for patient {}", patientId);
    }

    @Override
    @Consume(uri = "activemq:Consumer.Audit.VirtualTopic.Ripple.Procedures.Update")
    public void update(String patientId, ProcedureDetails procedure) {
        logger.info("AUDIT: Received procedure update request for patient {}", patientId);
    }
}
