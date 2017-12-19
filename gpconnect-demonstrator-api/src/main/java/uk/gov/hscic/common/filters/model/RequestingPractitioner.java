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

    @JsonProperty("name")
    private List<Name> name;

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

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public String getIdentifierValue(String system) {
        if (null == identifiers) {
            return null;
        }

        return identifiers.stream().filter(identifier -> identifier.getSystem().equals(system))
                .map(Identifier::getValue).findFirst().orElse(null);
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class Name {
    private String family;
    private List<String> given;
    private List<String> prefix;

    public String getFamily() {
        return this.family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public List<String> getGiven() {
        return this.given;
    }

    public void setGiven(List<String> given) {
        this.given = given;
    }

    public List<String> getPrefix() {
        return this.prefix;
    }

    public void setPrefix(List<String> prefix) {
        this.prefix = prefix;
    }
}