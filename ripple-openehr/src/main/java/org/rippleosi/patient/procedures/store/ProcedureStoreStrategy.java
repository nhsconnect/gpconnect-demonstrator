package org.rippleosi.patient.procedures.store;

import java.util.Collections;
import java.util.Map;

import org.rippleosi.common.service.UpdateStrategy;
import org.rippleosi.patient.procedures.model.ProcedureDetails;

/**
 */
public class ProcedureStoreStrategy implements UpdateStrategy<ProcedureDetails> {

    private final String patientId;
    private final String compositionId;
    private final String template;
    private final Map<String,String> content;

    public ProcedureStoreStrategy(String patientId, String template, Map<String, String> content) {
        this(null, patientId, template, content);
    }

    public ProcedureStoreStrategy(String compositionId, String patientId, String template, Map<String, String> content) {
        this.compositionId = compositionId;
        this.patientId = patientId;
        this.template = template;
        this.content = Collections.unmodifiableMap(content);
    }

    @Override
    public String getCompositionId() {
        return compositionId;
    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public Map<String, String> getContent() {
        return content;
    }
}
