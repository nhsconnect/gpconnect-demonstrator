package uk.gov.hscic.common.filters.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Parameter {
    private String name;
    private Identifier valueIdentifier;
    private Map<String, List<Coding>> valueCodeableConcept;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Identifier getValueIdentifier() {
        return valueIdentifier;
    }

    public void setValueIdentifier(Identifier valueIdentifier) {
        this.valueIdentifier = valueIdentifier;
    }

    public Map<String, List<Coding>> getValueCodeableConcept() {
        return valueCodeableConcept;
    }

    public void setValueCodeableConcept(Map<String, List<Coding>> valueCodeableConcept) {
        this.valueCodeableConcept = valueCodeableConcept;
    }
}
