package org.rippleosi.common.service;

import java.util.Collections;
import java.util.Map;

/**
 */
public class DefaultStoreStrategy implements UpdateStrategy {

    private final String patientId;
    private final String compositionId;
    private final String template;
    private final Map<String,Object> content;

    public DefaultStoreStrategy(String patientId, String template, Map<String, Object> content) {
        this(null, patientId, template, content);
    }

    public DefaultStoreStrategy(String compositionId, String patientId, String template, Map<String, Object> content) {
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
    public Map<String, Object> getContent() {
        return content;
    }
}
