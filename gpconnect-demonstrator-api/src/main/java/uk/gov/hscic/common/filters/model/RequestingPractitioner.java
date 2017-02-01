package uk.gov.hscic.common.filters.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestingPractitioner {
    private String id;
    private String resourceType;
    private Map<String, List<String>> name;

    @JsonProperty("practitionerRole")
    private List<Map<String, Map<String, List<Coding>>>> practitionerRoles;

    @JsonProperty("identifier")
    private List<Identifier> identifiers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Map<String, List<String>> getName() {
        return name;
    }

    public void setName(Map<String, List<String>> name) {
        this.name = name;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public String getIdentifierValue(String system) {
        return identifiers
                .stream()
                .filter(identifier -> identifier.getSystem().equals(system))
                .map(Identifier::getValue)
                .findFirst()
                .orElse(null);
    }

    public String getPractitionerRoleCode(String system) {
        if (practitionerRoles.isEmpty()) {
            return null;
        }

        return practitionerRoles
                .get(0)
                .getOrDefault("role", Collections.emptyMap())
                .getOrDefault("coding", Collections.emptyList())
                .stream()
                .filter(coding -> system.equals(coding.getSystem()))
                .map(Coding::getCode)
                .findFirst()
                .orElse(null);
    }

    public void setPractitionerRoles(List<Map<String, Map<String, List<Coding>>>> practitionerRoles) {
        this.practitionerRoles = practitionerRoles;
    }
}
