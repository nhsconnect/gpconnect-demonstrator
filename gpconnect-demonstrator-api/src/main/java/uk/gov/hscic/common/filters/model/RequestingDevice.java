package uk.gov.hscic.common.filters.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestingDevice {
    private String id;
    private String resourceType;
    private String model;
    private String version;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {    
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    /**
     * @return the identifiers
     */
    public List<Identifier> getIdentifiers() {
        return identifiers;
    }
}
