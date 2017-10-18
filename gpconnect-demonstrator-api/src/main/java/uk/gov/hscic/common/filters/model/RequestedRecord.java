package uk.gov.hscic.common.filters.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestedRecord {
    private String resourceType;

    private String id;
    
    @JsonProperty("identifier")
    private List<Identifier> identifiers;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
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
    
    public String getId(){
        return id;
    }
}
