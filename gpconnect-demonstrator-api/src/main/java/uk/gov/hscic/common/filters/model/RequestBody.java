package uk.gov.hscic.common.filters.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class RequestBody {
    private String resourceType;

    @JsonProperty("parameter")
    private List<Parameter> parameters;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getIdentifierParameterValue(String system) {
        return parameters
                .stream()
                .map(Parameter::getValueIdentifier)
                .filter(Objects::nonNull)
                .filter(identifier -> system.equals(identifier.getSystem()))
                .map(Identifier::getValue)
                .findFirst()
                .orElse(null);
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
