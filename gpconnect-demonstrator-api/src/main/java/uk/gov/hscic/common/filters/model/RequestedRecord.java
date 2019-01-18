package uk.gov.hscic.common.filters.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestedRecord {
    private String id;
    private String resourceType;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public String getIdentifierValue(String system) {
        if (null == identifiers) {
            return null;
        }
        
        return identifiers
                .stream()
                .filter(identifier -> identifier.getSystem().equals(system))
                .map(Identifier::getValue)
                .findFirst()
                .orElse(null);
    }
}
