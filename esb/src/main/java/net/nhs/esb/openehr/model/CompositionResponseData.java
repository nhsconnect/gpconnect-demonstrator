package net.nhs.esb.openehr.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 */
public class CompositionResponseData {

    private Meta meta;

    @JsonProperty("format")
    private String compositionFormat;
    private String templateId;
    private Map<String,Object> composition;
    private boolean deleted;
    private boolean lastVersion;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getCompositionFormat() {
        return compositionFormat;
    }

    public void setCompositionFormat(String compositionFormat) {
        this.compositionFormat = compositionFormat;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Map<String, Object> getComposition() {
        return composition;
    }

    public void setComposition(Map<String, Object> composition) {
        this.composition = composition;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(boolean lastVersion) {
        this.lastVersion = lastVersion;
    }
}
