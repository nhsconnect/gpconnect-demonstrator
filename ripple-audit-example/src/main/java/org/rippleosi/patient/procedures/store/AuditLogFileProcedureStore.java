package org.rippleosi.patient.procedures.store;

import org.apache.camel.Consume;
import org.rippleosi.patient.procedures.model.ProcedureDetails;
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
    public String getSource() {
        return "audit";
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
